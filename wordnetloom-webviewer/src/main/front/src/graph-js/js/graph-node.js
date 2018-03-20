"use strict";

var consts = {
  defaultType: {
    color: "green"
  }

};



var GraphNode = function(json, parentRef, expandedTop, expandedRight, expandedLeft, expandedBottom){
  var thisNode = this;

  if(json){
    thisNode.parentId = json.parentId || null;
    thisNode.parent = parentRef || null;

    thisNode.id = json.id;
    thisNode.title = json.title;
    thisNode.partOfSpeechId = json.partOfSpeechId;

    if(json.type)
      thisNode.type = json.type || consts.defaultType;
    else{
      thisNode.assignColorBasedFromPartOfSpeech();
    }
    thisNode.x = json.x;
    thisNode.y = json.y;

    thisNode.childrenTop = json.childrenTop;
    thisNode.childrenRight= json.childrenRight;
    thisNode.childrenBottom= json.childrenBottom;
    thisNode.childrenLeft= json.childrenLeft;

  }
  thisNode.selected = false;
  thisNode.childrenTopRef = [];
  thisNode.childrenRightRef= [];
  thisNode.childrenBottomRef= [];
  thisNode.childrenLeftRef = [];

  thisNode.expandedTop = expandedTop || false;
  thisNode.expandedRight = expandedRight || false;
  thisNode.expandedLeft = expandedLeft || false;
  thisNode.expandedBottom = expandedBottom || false;


  thisNode.childrenDownloaded = false;

};

GraphNode.prototype.markSelected = function(){
  var thisNode = this;
  thisNode.selected = true;
};

GraphNode.prototype.unmarkSelected = function(){
  var thisNode = this;
  thisNode.selected = false;
};

GraphNode.prototype.assignColorBasedFromPartOfSpeech = function () {
  var thisNode = this;
  var color = '#F0E68C'; // default

  switch (thisNode.partOfSpeechId){
    case 1: // verb, czasownik plWN czasownik
      color = '#FED25C';
      break;
    case 2: // noun, rzeczownik
      color = '#ABFFAE';
      break;
    case 3: // adverb, przysłówek plWN przysłówek
      color = '#ABFFAE';
      break;
    case 4: // adjective, przymiotnik
      color = '#ACFFEA';
      break;
    case 5: // verb, czasownik PWN czasownik
      color = '#FED25C';
      break;
    case 6: // noun, rzeczownik PWN rzeczownik
      color = '#ABFFAE';
      break;
    case 7: // adverb, przysłówek PWN przysłówek
      color = '#B3CDEA';
      break;
    case 8: // adjective, przymiotnik PWN przymiotnik
      color = '#ACFFEA';
      break;
  }

  thisNode.type = {
    color: color
  }
};

GraphNode.prototype.nodeType = 'ordinary';

GraphNode.prototype.getType = function(){
  return "ordinary";
};

GraphNode.prototype.api = new ApiConnector();

GraphNode.prototype.clearChildrenRef = function(position){
  var thisNode = this;
  switch(position){
    case 0:
      thisNode.childrenTopRef = [];
      break;
    case 1:
      thisNode.childrenRightRef = [];
      break;
    case 2:
      thisNode.childrenBottomRef = [];
      break;
    case 3:
      thisNode.childrenLeftRef = [];
      break;
  }
};
GraphNode.prototype.addCumulativeChildrenRef = function (cumulativeNodes) {
  var thisNode = this;

  if(cumulativeNodes.top) thisNode.childrenTopRef.push(cumulativeNodes.top);
  if(cumulativeNodes.right) thisNode.childrenRightRef.push(cumulativeNodes.right);
  if(cumulativeNodes.bottom) thisNode.childrenBottomRef.push(cumulativeNodes.bottom);
  if(cumulativeNodes.left) thisNode.childrenLeftRef.push(cumulativeNodes.left);
};

GraphNode.prototype.convertChildrenToRef =function(possibleNodesPool){
  var thisNode = this;

  var comparator = function(x){
    return x.target === nodeId;
  };

  for(var i =0 ; i< possibleNodesPool.length; i++){
    var nodeId= possibleNodesPool[i].id;

    var child = thisNode.childrenTop.find(comparator);
    if(child){
      thisNode.childrenTopRef.push(possibleNodesPool[i]);
      continue;
    }
    child = thisNode.childrenRight.find(comparator);
    if(child){
        thisNode.childrenRightRef.push(possibleNodesPool[i]);
        continue;
    }
    child = thisNode.childrenBottom.find(comparator);
    if(child){
        thisNode.childrenBottomRef.push(possibleNodesPool[i]);
        continue;
    }
    child = thisNode.childrenLeft.find(comparator);
    if(child){
        thisNode.childrenLeftRef.push(possibleNodesPool[i]);
    }
  }
};

GraphNode.prototype.consts = {
  DIRECTIONS: {
    TOP: 0,
    RIGHT: 1,
    BOTTOM: 2,
    LEFT: 3
  }
};

GraphNode.prototype.getChildrenWithCallback = function(callback){
  var thisNode = this;
  thisNode.api.getGraph(thisNode.id, function(d){
    callback(d);
  });
};

GraphNode.prototype.expandTopClick = function(allNodes, callback){
  var thisNode = this;
  thisNode.expandedTop = true;

  thisNode.findNewPlace(allNodes);
  thisNode.getChildrenWithCallback(callback);
};

GraphNode.prototype.expandRightClick = function (allNodes, callback) {
    var thisNode = this;
    thisNode.expandedRight = true;

    thisNode.findNewPlace(allNodes);
    thisNode.getChildrenWithCallback(callback);

};

GraphNode.prototype.expandBottomClick = function (allNodes, callback) {
    var thisNode = this;
    thisNode.expandedBottom = true;

    thisNode.findNewPlace(allNodes);
    thisNode.getChildrenWithCallback(callback);
};

GraphNode.prototype.expandLeftClick = function (allNodes, callback) {
    var thisNode = this;
    thisNode.expandedLeft = true;

    thisNode.findNewPlace(allNodes);
    thisNode.getChildrenWithCallback(callback);
};

GraphNode.prototype.moveChildren = function (newPointsOffsetArr) {
    var thisNode = this;
    var children = thisNode.getAllChildren();
    for(var i =0; i< children.length; i++){
        var offset = [children[i].x - newPointsOffsetArr[0], children[i].y - newPointsOffsetArr[1]];
        children[i].assignNewPosition(offset);
        children[i].moveChildren(newPointsOffsetArr);
    }
};

GraphNode.prototype.getChildrenAtPosition = function(pos){
  var thisNode = this;
  switch(pos){
    case 0:
      return thisNode.childrenTopRef;
      break;
    case 1:
      return thisNode.childrenRightRef;
      break;
    case 2:
      return thisNode.childrenBottomRef;
      break;
    case 3:
      return thisNode.childrenLeftRef;
      break;

  }
};

GraphNode.prototype.getAllChildren= function(){
  var thisNode = this;
  var children = thisNode.childrenTopRef.concat(
      thisNode.childrenRightRef,
      thisNode.childrenBottomRef,
      thisNode.childrenLeftRef);
  return children;
};

GraphNode.prototype.assignNewPosition = function(newPointsArr){
  var thisNode=this;
  thisNode.x = newPointsArr[0];
  thisNode.y = newPointsArr[1];
};

GraphNode.prototype.findNewPlace = function(allNodes){
  var thisNode = this;

  var newPoints = [thisNode.x, thisNode.y];
  var offset = 20;
  var max_iter = 10;
  while(!thisNode.checkIfSpaceInRegion(newPoints, allNodes) && max_iter > 0){
    newPoints = thisNode.calculatePossibleNewPosition(offset);
    offset += 20;
    max_iter--;
  }
  thisNode.moveChildren([thisNode.x - newPoints[0], thisNode.y - newPoints[1]]);
  thisNode.assignNewPosition(newPoints);
  return newPoints;
};

GraphNode.prototype.checkIfSpaceInRegion = function(newPointsArr, allNodes){
  var thisNode = this;

  // TODO this has to be dependent on number of new nodes!
  var paddingVertical = 500;
  var paddingHorizontal = 1000;


  var x = newPointsArr[0];
  var y = newPointsArr[1];

  var children = thisNode.getAllChildren();

  for(var i =0; i < allNodes.length; i++){
    if(children.indexOf(allNodes[i]) > -1) continue;

    var compareX = allNodes[i].x;
    var compareY = allNodes[i].y;

    if(thisNode.expandedTop){
      if(thisNode.checkIfPointInRange(compareX, compareY,
        x - paddingHorizontal /2, x + paddingHorizontal/2,
        y-paddingVertical, y)){
          return false;
        }
    }
    if(thisNode.expandedRight){
      if(thisNode.checkIfPointInRange(compareX, compareY,
        x, x + paddingHorizontal,
        y - paddingVertical /2, y + paddingVertical /2)){
          return false;
        }
    }
    if(thisNode.expandedBottom){
      if(thisNode.checkIfPointInRange(compareX, compareY,
        x - paddingHorizontal /2, x + paddingHorizontal/2,
        y, y + paddingVertical)){
          return false;
        }
    }
    if(thisNode.expandedLeft){
      if(thisNode.checkIfPointInRange(compareX, compareY,
        x - paddingHorizontal, x,
        y - paddingVertical /2, y + paddingVertical /2)){
          return false;
        }
    }
  }
  return true;
};

GraphNode.prototype.checkIfPointInRange = function(x,y,minX, maxX, minY, maxY){
  var resultX = (x > minX && x < maxX);
  var resultY = (y > minY && y < maxY);
  return resultX && resultY;
};

GraphNode.prototype.calculatePossibleNewPosition = function(offset){
  var thisNode = this;
  var parent = thisNode.parent;

  var deltaX = parent.x - thisNode.x;
  var deltaY = parent.y - thisNode.y;

  var newX = thisNode.x;
  var newY = thisNode.y;

  var ratio = deltaY / deltaX;


  if(deltaX === 0 || Math.abs(deltaX) < 50 ){
      newY = deltaY > 0 ? thisNode.y - offset : thisNode.y + offset;
  } else if( deltaY === 0 && deltaX < 0){
      newX = thisNode.x + offset;
  } else if(deltaY < 0){
    if(ratio < 0){
      newX = thisNode.x - offset;
      newY = thisNode.y - offset * ratio;
    } else {
      newX = thisNode.x + offset;
      newY = thisNode.y + offset * ratio;
    }
  } else {
    if(ratio < 0){
      newX = thisNode.x + offset;
      newY = thisNode.y + offset * ratio;
    } else {
      newX = thisNode.x - offset;
      newY = thisNode.y - offset * ratio;
    }
  }

  return [newX, newY];
};

GraphNode.prototype.calculatePossibleNewPositionABC= function(offset){
  var thisNode = this;
  var parent = thisNode.parent;

  var deltaX = parent.x - thisNode.x;
  var deltaY = parent.y - thisNode.y;

  var newX = thisNode.x;
  var newY = thisNode.y;

  var ratio = deltaY / deltaX;


  if(deltaY === 0){
    newX = deltaX > 0 ? thisNode.x - offset : thisNode.x + offset;
  } else if( deltaX === 0 && deltaY < 0){
    newY = thisNode.y + offset;
  } else if(deltaX < 0){
    if(ratio < 0){
      newY = thisNode.y - offset;
      newX = thisNode.x - offset * ratio;
    } else {
      newY = thisNode.y + offset;
      newX = thisNode.x + offset * ratio;
    }
  } else {
    if(ratio < 0){
      newY = thisNode.y + offset;
      newX = thisNode.x + offset * ratio;
    } else {
      newY = thisNode.y - offset;
      newX = thisNode.x - offset * ratio;
    }
  }
  return [newX, newY];
/*
  var thisNode = this;
  var parent = thisNode.parent;
  // trying with f(x) = ax + b
  var deltaX = parent.x - thisNode.x;
  var deltaY = parent.y - thisNode.y;

  var x,y;

  if(deltaX === 0){
    if(deltaY > 0)
      return [thisNode.x, thisNode.y - offset];
    else
      return [thisNode.x, thisNode.y + offset];
  }
  if(deltaY === 0){
    if(deltaX > 0)
      return [thisNode.x - offset, thisNode.y];
    else
      return [thisNode.x + offset, thisNode.y];
  }

  var a = deltaY / deltaX;
  var b = thisNode.y - a * thisNode.x;


  if(a > 0)
    x = thisNode.x + offset/Math.sqrt(1 + Math.pow(a,2));
  else
    x = thisNode.x - offset/Math.sqrt(1 + Math.pow(a,2));

  console.log(deltaY);
    y = a * x + b;
  if(deltaY > 0)
    y = -a * x + b;



  console.log(a, b);
  // console.log(x, y);
  return [x,y];
  */
};

GraphNode.prototype.moveAwayFromParent = function(parent, offset){
  var thisNode = this;
  var deltaX = parent.x - thisNode.x;
  var deltaY = parent.y - thisNode.y;

  var ratio = deltaY / deltaX;

  if(deltaX === 0){
      thisNode.y = deltaY > 0 ? thisNode.y - offset : thisNode.y + offset;
  } else if( deltaY === 0 && deltaX < 0){
      thisNode.x = thisNode.x + offset;
  } else if(deltaY < 0){
    if(ratio < 0){
      thisNode.x = thisNode.x - offset;
      thisNode.y = thisNode.y - offset * ratio;
    } else {
      thisNode.x = thisNode.x + offset;
      thisNode.y = thisNode.y + offset * ratio;
    }
  } else {
    if(ratio < 0){
      thisNode.x = thisNode.x + offset;
      thisNode.y = thisNode.y + offset * ratio;
    } else {
      thisNode.x = thisNode.x - offset;
      thisNode.y = thisNode.y - offset * ratio;
    }
  }
};

GraphNode.prototype.addNewConnections = function(json){
  var thisNode = this;

  thisNode.childrenTop = thisNode.childrenTop.concat(json.masterNode.childrenTop);
  thisNode.childrenRight = thisNode.childrenRight.concat(json.masterNode.childrenRight);
  thisNode.childrenBottom = thisNode.childrenBottom.concat(json.masterNode.childrenBottom);
  thisNode.childrenLeft = thisNode.childrenLeft.concat(json.masterNode.childrenLeft);
};

GraphNode.prototype.isNodeCumulator = function(){
  return false;
};


GraphNode.prototype.expandTriangleClick = function(graph, triangleHandle, place){
  var thisNode = this;
  var expandedIndicator = ['expandedTop', 'expandedRight', 'expandedBottom', 'expandedleft'][place];
  var expandedFunction = ['expandTopClick', 'expandRightClick', 'expandBottomClick', 'expandLeftClick'][place];
  var children = ['childrenTop', 'childrenRight', 'childrenBottom', 'childrenLeft'][place];

  // if node at given position is not expanded
  if(!thisNode[expandedIndicator]){
    thisNode[expandedFunction](graph.nodes, function (data) {
      graph.expandNode(thisNode, data, thisNode[children]);
      d3.select(triangleHandle).classed("expanded", true);
    });
  }
  else {
    thisNode[expandedIndicator] = false;
    graph.removeNodeChildren(thisNode, 0);
    d3.select(triangleHandle).classed("expanded", false);
  }

};

var GraphNodeCumulator = function (json, parentConnection, hiddenNodes) {
  var thisNode = this;
  GraphNode.call(this, json, parentConnection);
  thisNode.hiddenNodes = hiddenNodes || [];
  thisNode.title = thisNode.hiddenNodes.length;

  thisNode.childrenTop = [];
  thisNode.childrenRight= [];
  thisNode.childrenBottom= [];
  thisNode.childrenLeft= [];
};

GraphNodeCumulator.prototype = new GraphNode();
GraphNodeCumulator.prototype.constructor = GraphNodeCumulator;
GraphNodeCumulator.prototype.nodeType = 'cumulator';

GraphNodeCumulator.prototype.getType = function(){
  return "cumulator";
};

GraphNodeCumulator.prototype.getHiddenNodes = function(){
  var thisNode = this;
  return thisNode.hiddenNodes;
};

GraphNodeCumulator.prototype.updateCount = function(){
  var thisNode = this;
  thisNode.title = thisNode.hiddenNodes.length;
};

GraphNodeCumulator.prototype.addHiddenNode = function(node, edge){
  var thisNode = this;
  thisNode.hiddenNodes.push({node: node, edge: edge});
  thisNode.updateCount();
};

GraphNodeCumulator.prototype.expandHiddenNode = function(graph, hiddenRef){
  var thisNode = this;

  var hiddenRefParent = graph.nodes.filter(function(x){return x.id === hiddenRef.node.parentId})[0];

  var newNode = new GraphNode(hiddenRef.node, hiddenRefParent);
  newNode.x = thisNode.x + 100;
  newNode.y = thisNode.y + 100;

  graph.nodes.push(newNode);
  graph.setIdCt += 1;
  hiddenRef.edge.source = hiddenRefParent;
  hiddenRef.edge.target = newNode;
  hiddenRef.edge.connectionPoints = hiddenRef.edge.connectedTo;

  var edgeDetails = graph.relations.getEdgeDetails(hiddenRef.edge.rel);

  hiddenRef.edge.pathTextSrc= edgeDetails.textSrc;
  hiddenRef.edge.pathTextTarget= edgeDetails.textTarget;
  hiddenRef.edge.color= edgeDetails.color;
  hiddenRef.edge.dotted= edgeDetails.dotted;

  graph.edges.push(hiddenRef.edge);

  // updating other possible edges
  var edges = [];

  edges = edges.concat(graph.getPossibleNonMasterEdgesJson(newNode, newNode.childrenTop, [0,2]));
  edges = edges.concat(graph.getPossibleNonMasterEdgesJson(newNode, newNode.childrenRight, [1,3]));
  edges = edges.concat(graph.getPossibleNonMasterEdgesJson(newNode, newNode.childrenBottom, [2,0]));
  edges = edges.concat(graph.getPossibleNonMasterEdgesJson(newNode, newNode.childrenLeft, [3,1]));

  for(var i =0; i < edges.length; i++){
    var e = graph.initializeEdge(edges[i]);
    if(e !== null)
      graph.edges.push(e);
  }


  graph.updateGraph();

  thisNode.hiddenNodes.splice(thisNode.hiddenNodes.indexOf(hiddenRef), 1);

  graph.hiddenList
    .style("display", "none");
};

GraphNodeCumulator.prototype.addOnclickToCreatedNodes = function(graph, hiddenNodesIds, hiddenNodesRef){
  var thisNode = this;

  for(var i = 0; i < hiddenNodesIds.length; i++){
    (function(index) {
      var nodeId = hiddenNodesIds[index];
      d3.select(nodeId).on('click', function () {
        thisNode.expandHiddenNode(graph, hiddenNodesRef[index]);
      })
    })(i);
  }
};

GraphNodeCumulator.prototype.isNodeCumulator = function(){
  return true;
};

GraphNodeCumulator.prototype.getHiddenListHtml = function(){
  var thisNode = this;
  var ret = {
    html: "",
    createdIds: [],
    hiddenRef: []
  };
  ret.html = "<ul class='hidden-nodes-list'>";
  for(var i = 0; i < thisNode.hiddenNodes.length; i++){
    var n = thisNode.hiddenNodes[i].node;
    ret.html += "\n<li id='hidden-"+n.id+"'>" + n.title + "</li>";
    ret.createdIds.push("#hidden-"+n.id);
    ret.hiddenRef.push(thisNode.hiddenNodes[i]);
  }
  ret.html += "\n</ul>";
  return ret;
};
