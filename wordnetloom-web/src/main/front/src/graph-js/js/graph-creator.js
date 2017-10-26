  "use strict";

  // TODO add user settings
  var consts = {
    defaultTitle: "random variable"
  };
  var settings = {
    appendElSpec: ".inner-graph-container",
    sensesListId: ".possible-senses",
    loadingAnimation: ".graph-loader"
  };
  // define graphcreator object
  var GraphCreator = function(containerId, showSearchBox, width, height) {
    var thisGraph = this;

    thisGraph.width = width;
    thisGraph.height = height;

    var toolboxHtml = '<div id="toolbox">' +
      '<form id="inspected-word-form">' +
      '<button type="submit" value="Submit" id="inspected-word-btn">Szukaj</button>' +
      '<input type="text" id="inspected-word" placeholder="Słowo którego szukasz&hellip;">' +
      '</form>' +
      '<ul class="possible-senses" style="display: none"></ul>' +
      '</div>';

    var initHtml ='<div class="graph-loader" style="display: none"></div><div class="inner-graph-container"></div>';

    if(showSearchBox){
      initHtml += toolboxHtml;
    }
    var graphContainer = d3.select('#'+containerId).html(initHtml);

    var appendElHandle = graphContainer.select(settings.appendElSpec);

    var svg = appendElHandle.append("svg")
      .attr("width", width)
      .attr("height", height);

    thisGraph.hiddenList = appendElHandle.append("div")
      .attr('pointer-events', 'none')
      .attr("class", "hidden-list tooltip")
      .style("opacity", 1)
      .html("FIRST LINE <br> SECOND LINE")
      .style("display", "none");

    appendElHandle.append("button").attr('id', 'download-picture');

    thisGraph.sensesList = d3.select(settings.sensesListId);

    thisGraph.tooltip = d3.select(settings.appendElSpec).append("div")
      .attr("id", "node-tooltip")
      .style("opacity", 0);

    thisGraph.loadingAnimationHandle = d3.select(settings.loadingAnimation)
      .style("display", "none");

    thisGraph.idct = 0;

    thisGraph.nodes = [];
    thisGraph.edges = [];

    thisGraph.transformed = {
      x: 0,
      y: 0,
      k: 1
    };

    thisGraph.state = {
      selectedNode: null,
      selectedEdge: null,
      mouseDownNode: null,
      mouseDownLink: null,
      justDragged: false,
      justScaleTransGraph: false,
      lastKeyDown: -1,
      shiftNodeDrag: false,
      selectedText: null,
      brushSelect: false
    };

    // for event dispatcher
    thisGraph.eventDispatch = d3.dispatch("ordinary-node-mousedown");

    // TODO move to one file
    thisGraph.relations = new Relations();
    thisGraph.api = new ApiConnector();


    // define arrow markers for graph links
    var defs = svg.append('svg:defs');
    defs.append('svg:marker')
      .attr('id', 'end-arrow')
      .attr('viewBox', '0 -5 10 10')
      .attr('refX', "8")
      .attr('markerWidth', 5)
      .attr('markerHeight', 5)
      .attr('orient', 'auto')
      .append('svg:path')
      .attr('d', 'M0,-5L10,0L0,5');

    // define starting arrow
    defs.append('svg:marker')
      .attr('id', 'start-arrow')
      .attr('viewBox', '0 0 10 10')
      .attr('refX', 2)
      .attr('refY', 5)
      .attr('markerWidth', 5)
      .attr('markerHeight', 5)
      .attr('orient', 'auto')
      .append('svg:path')
      .attr('d', 'M0,5L10,0L10,10');

    // define arrow markers for leading arrow
    defs.append('svg:marker')
      .attr('id', 'mark-end-arrow')
      .attr('viewBox', '0 -5 10 10')
      .attr('refX', 7)
      .attr('markerWidth', 3.5)
      .attr('markerHeight', 3.5)
      .attr('orient', 'auto')
      .append('svg:path')
      .attr('d', 'M0,-5L10,0L0,5');

    thisGraph.svg = svg;
    thisGraph.svgG = svg.append("g")
      .classed(thisGraph.consts.graphClass, true);
    var svgG = thisGraph.svgG;

    // displayed when dragging between nodes
    thisGraph.dragLine = svgG.append('svg:path')
      .attr('class', 'link dragline hidden')
      .attr('d', 'M0,0L0,0')
      .style('marker-end', 'url(#mark-end-arrow)');

    // svg nodes and edges
    thisGraph.paths = svgG.append("g").attr("id", "all-paths-id").selectAll("g");
    thisGraph.pathsText = svgG.append("g").attr("id", "all-paths-text-id").selectAll("g");
    thisGraph.boats = svgG.append("g").attr("id", "all-boats-id").selectAll("g");

    thisGraph.drag = d3.drag()
      .subject(function(d) {
        return {
          x: d.x,
          y: d.y
        };
      })
      .on("drag", function(args) {
        thisGraph.state.justDragged = true;
        thisGraph.dragmove.call(thisGraph, args);
      });

    // listen for key events
    d3.select(window).on("keydown", function() {
        thisGraph.svgKeyDown.call(thisGraph);
      })
      .on("keyup", function() {
        thisGraph.svgKeyUp.call(thisGraph);
      });
    svg.on("mousedown", function(d) {
      thisGraph.svgMouseDown.call(thisGraph, d);
    });
    svg.on("mouseup", function(d) {
      thisGraph.svgMouseUp.call(thisGraph, d);
    });

    // listen for dragging
    // var dragSvg = d3.behavior.zoom()
    var dragSvg = d3.zoom()
      .on("zoom", function() {
        if (d3.event.sourceEvent.shiftKey) {
          // TODO  the internal d3 state is still changing
          return false;
        }
        else {
          thisGraph.zoomed.call(thisGraph);
        }
        return true;
      })
      .on("start", function() {
        var ael = d3.select("#" + thisGraph.consts.activeEditId).node();
        if (ael) {
          ael.blur();
        }
        if (!d3.event.sourceEvent.shiftKey) d3.select('body').style("cursor", "move");
      })
      .on("end", function() {
        d3.select('body').style("cursor", "auto");
      });

    svg.call(dragSvg).on("dblclick.zoom", null);

    // listen for resize
    window.onresize = function() {
      // thisGraph.updateWindow(svg);
    };


    var brushed = function(){
      thisGraph.brushed();
    };
    thisGraph.brush = d3.brush();
    thisGraph.brush.graph = this;
    thisGraph.brush.on("brush", brushed);

    // handle download data
    d3.select("#download-input").on("click", function() {
      var saveEdges = [];
      thisGraph.edges.forEach(function(val, i) {
        saveEdges.push({
          source: val.source.id,
          target: val.target.id
        });
      });
      var blob = new Blob([window.JSON.stringify({
        "nodes": thisGraph.nodes,
        "edges": saveEdges
      })], {
        type: "text/plain;charset=utf-8"
      });
      saveAs(blob, "mydag.json");
    });

    // searching for word
    d3.select('#inspected-word-form').on('submit', function(){
      d3.event.preventDefault();

      console.log('on submit');

      var input = d3.select("#inspected-word");
      thisGraph.loadNewWord(input.node().value);
    });

    // handle download picture
    d3.select('#download-picture').on('click', function(){
      thisGraph.exportVisualization();
    });

    // handle uploaded data
    d3.select("#upload-input").on("click", function() {
      document.getElementById("hidden-file-upload").click();
    });
    d3.select("#hidden-file-upload").on("change", function() {
      if (window.File && window.FileReader && window.FileList && window.Blob) {
        var uploadFile = this.files[0];
        var filereader = new window.FileReader();

        filereader.onload = function() {
          var txtRes = filereader.result;
          // TODO better error handling
          try {
            var jsonObj = JSON.parse(txtRes);
            thisGraph.deleteGraph(true);
            thisGraph.nodes = jsonObj.nodes;
            thisGraph.setIdCt(jsonObj.nodes.length + 1);
            var newEdges = jsonObj.edges;
            newEdges.forEach(function(e, i) {
              newEdges[i] = {
                source: thisGraph.nodes.filter(function(n) {
                  return n.id === e.source;
                })[0],
                target: thisGraph.nodes.filter(function(n) {
                  return n.id === e.target;
                })[0],
                connectionPoints: e.connectedTo,
                rel: e.rel,
                // todo get relation type at this point
                pathTextSrc: edgeDetails.textSrc,
                pathTextTarget: edgeDetails.textTarget,
                color: edgeDetails.color,
                dotted: edgeDetails.dotted
              };
            });
            thisGraph.edges = newEdges;
            thisGraph.updateGraph();
          }
          catch (err) {
            window.alert("Error parsing uploaded file\nerror message: " + err.message);
          }
        };
        filereader.readAsText(uploadFile);

      }
      else {
        alert("Your browser won't let you save this graph -- try upgrading your browser to IE 10+ or Chrome or Firefox.");
      }

    });

    // handle delete graph
    d3.select("#delete-graph").on("click", function() {
      thisGraph.deleteGraph(false);
    });

  };

  GraphCreator.prototype.showLoadingAnimation = function () {
    var thisGraph = this;
    thisGraph.loadingAnimationHandle.style("display", "block");
  };

  GraphCreator.prototype.hideLoadingAnimation = function () {
    var thisGraph = this;
    thisGraph.loadingAnimationHandle.style("display", "none");
  };

  GraphCreator.prototype.listPossibleSenses = function(senses){
    var thisGraph = this;
    thisGraph.hideLoadingAnimation();
    if(senses){
      var callback = function(d){
        if(d){
         thisGraph.initializeFromSynsetId(d.id);
        }
      };
      if(senses.length ===0){
        thisGraph.sensesList
          .style('display', 'block')
          .html("") // clear list
          .html("<li>nic nie znaleziono</li>");
      }
      // if only one sense found, display it immediately
      else if(senses.length === 1){
        thisGraph.api.getSynsetFromSenseId(senses[0].id, callback);
      }
      // list possible options
      else {
        var listHtml = "";
        for(var i = 0; i < senses.length; i++){
          listHtml += '<li class="hidden-list-item" data-sense-id="'+senses[i].id+'"'+">"+senses[i].lemma.word+"</li>\n";
        }
        thisGraph.sensesList
          .style('display', 'block')
          .html("") // clear list
          .html(listHtml);

        var list = d3.selectAll(".hidden-list-item")
          .on('click', function(){
            var senseId = d3.select(this).attr("data-sense-id");
            thisGraph.api.getSynsetFromSenseId(senseId, callback);
          });
      }
    }
  };

  GraphCreator.prototype.loadNewWord = function(word){
    var thisGraph = this;
    thisGraph.showLoadingAnimation();
    // pass listPossibleSenses as callback
    thisGraph.api.getSensesFromLemma(word, thisGraph.listPossibleSenses.bind(thisGraph));
  };

  GraphCreator.prototype.initFromJson = function(jsonObj, masterNodeJson, cumulativeNodes) {
    var thisGraph = this;
    try {
      thisGraph.deleteGraph(true);

      var masterNode = new GraphNode(masterNodeJson, null,true, true,true,true);
      thisGraph.nodes.push(masterNode);
      thisGraph.setIdCt += 1;
      thisGraph.updateWithNewNodes(jsonObj, masterNode, cumulativeNodes);

    }
    catch (err) {
      window.alert("Error parsing json\nerror message: " + err.message);
    }
  };

  GraphCreator.prototype.edgeNotExisting = function(first, second, additionalArrEdges){
    var thisGraph = this;
    for(var i=0; i< thisGraph.edges.length; i++){
      var edge = thisGraph.edges[i];
      if(edge.source === first && edge.target === second)
        return false;
      if(edge.source === second && edge.target === first)
        return false;
    }
    if (additionalArrEdges !== undefined){
      for(i=0; i < additionalArrEdges.length; i++){
        edge = additionalArrEdges[i];;
        if(edge.source === first && edge.target === second)
          return false;
        if(edge.source === second && edge.target === first)
          return false;
      }
    }
    return true;
  };
  /*
   * additionalArrNodes- contains nodes that are yet to be added to the graph
   * additionalArrEdges- contains edges that are yet to be added to the graph
   */
  GraphCreator.prototype.getPossibleNonMasterEdgesJson = function(node, children, connPoint, additionalArrNodes, additionalArrEdges){
    var thisGraph = this;
    var edges = [];
    // loop throught given children
    for(var i =0; i < children.length; i++){
      // search in ready graph nodes
      var child = children[i];
      if(thisGraph.nodes.find(function(x){ return(x.id === child.target);})){
        if (thisGraph.edgeNotExisting(node.id, child.target, additionalArrEdges)) {
          edges.push({
            source: node.id,
            target: child.target,
            connectedTo: connPoint,
            rel: child.rel
          });
        }
      } else if(additionalArrNodes && additionalArrNodes.find( function(x){ return(x.id === child.target);} )){
        if (thisGraph.edgeNotExisting(node.id, child.target, additionalArrEdges)) {
          edges.push({
            source: node.id,
            target: child.target,
            connectedTo: connPoint,
            rel: child.rel
          });
        }
      }
    }
    return edges;
  };

  // for api data
  GraphCreator.prototype.prepareJsonApi = function(jsonObj, initializing, masterNode){
    initializing = initializing || false;
    var thisGraph = this;
    var edges = [];

    if(initializing){
      var xLoc = thisGraph.width / 2 - 25,
        yLoc = thisGraph.height/2 - 100;

      masterNode.x = xLoc;
      masterNode.y = yLoc;
    }

    var calculateEllipseY = function(a, b, h, k, x, top){
      if (top) {
        return   k + Math.sqrt(b*b - (1-(x-h)*(x-h))/(a*a));
      } else {
        return   k - Math.sqrt(b*b - (1-(x-h)*(x-h))/(a*a));
      }
    };
    var calculateEllipseX = function(a, b, h, k, y, right){
      if (right){
        return h - Math.sqrt(b*b - (1-(y-k)*(y-k))/(a*a));
      } else {
        return h + Math.sqrt(b*b - (1-(y-k)*(y-k))/(a*a));
      }
    };
    var distributeNode = function(nodeSize, nodeNumber, numberOfAllChildren){
      var avaliableSpace = numberOfAllChildren * nodeSize * 2 ;
      var startPlace = - (avaliableSpace /2);
      var offset =  startPlace + nodeSize * 2 * (nodeNumber + 1) - nodeSize;
      return offset;
    };
    var assignPlace = function(place, masterNode, childNode, cnt, numberOfAllChildren) {
      numberOfAllChildren = numberOfAllChildren >= thisGraph.consts.maxDefaultNodesHorizontally ? thisGraph.consts.maxDefaultNodesHorizontally : numberOfAllChildren;
      switch (place) {
        case 'top':
          childNode.x = masterNode.x + distributeNode(thisGraph.consts.nodeWidth + 2, cnt, numberOfAllChildren);
          childNode.y = -200 + calculateEllipseY(8,1,masterNode.x,masterNode.y,childNode.x, true); //masterNode.y - thisGraph.consts.nodeDistance;
          break;
        case 'right':
          childNode.y = masterNode.y + distributeNode(thisGraph.consts.nodeHeight - 15, cnt, numberOfAllChildren);
          childNode.x = 200 + calculateEllipseX(3,1,masterNode.x,masterNode.y,childNode.y, true);  // thisGraph.consts.nodeDistance;
          break;
        case 'bottom':
          childNode.x = masterNode.x + distributeNode(thisGraph.consts.nodeWidth + 10, cnt, numberOfAllChildren);
          childNode.y = masterNode.y + thisGraph.consts.nodeDistance;
          childNode.y = 200 + calculateEllipseY(8,1,masterNode.x,masterNode.y,childNode.x, false);
          break;
        case 'left':
          childNode.x = masterNode.x - thisGraph.consts.nodeDistance;
          childNode.y = masterNode.y + distributeNode(thisGraph.consts.nodeHeight - 15, cnt, numberOfAllChildren);
          childNode.x = -200 + calculateEllipseX(3,1,masterNode.x,masterNode.y,childNode.y, false);
          break;
      }
    };

    var edgeNotExisting = function(first, second){
      for(var i=0; i< edges.length; i++){
        var edge = edges[i];
        if(edge.source === first && edge.target === second)
          return false;
        if(edge.source === second && edge.target === first)
          return false;
      }
      return true;
    };
    var addEdgesIfApplicable = function(nodeFrom, arr, connectionPoints){
      for(var j = 0; j < arr.length; j++){
        var child = arr[j];
        // checking if relation with masterNode
        if(child.target === masterNode.id)
          continue;

        // continue;
        // checking if other node exists
        if(thisGraph.nodes.find(function(x){ return(x.id === child.target);})) {
          // checking if relation doesn't already exist
          if (edgeNotExisting(nodeFrom.id, child.target)) {
            edges.push({
              source: n.id,
              target: child.target,
              connectedTo: connectionPoints,
              rel: child.rel
            })
          }
        }
        // todo - add relation to existing nodes
      }
    };
    var deleteEdgesWithTargetOrSource = function(id){
      var i = edges.length;
      while(i--){
        if(edges[i].target === id || edges[i].source === id){
          edges.splice(i,1);
        }
      }
    };
    var cnt = {
      top: 0,
      right: 0,
      bottom: 0,
      left: 0
    };
    var addNewNode = function(node, place, loopCnt, connectionTypeArr, rel, childrenLen, horizontally){
      var maxChildren = horizontally ? thisGraph.consts.maxDefaultNodesHorizontally : thisGraph.consts.maxDefaultNodesVertically;
      var edge = {
        source: masterNode.id,
        target: node.id,
        connectedTo: connectionTypeArr,
        rel: rel.rel
      };
      // console.log(masterNode.id, node.id, cnt[place], place);
      if(cnt[place] > maxChildren - 2 && childrenLen > maxChildren){
        jsonObj.nodes.splice(loopCnt, 1);
        deleteEdgesWithTargetOrSource(node.id);
        if(!cumulativeNodes[place]) {
          cumulativeNodes[place] = new GraphNodeCumulator({id: -node.id, type:{color: "green"}}, masterNode, []);
          rel.rel = 0;
          cumulativeNodes[place].addHiddenNode(node, edge);
          node = cumulativeNodes[place];
        } else {
          cumulativeNodes[place].addHiddenNode(node, edge);
          return;
        }
      }
      assignPlace(place, masterNode, node, cnt[place], childrenLen);
      edges.push({
        source: masterNode.id,
        target: node.id,
        connectedTo: connectionTypeArr,
        rel: rel.rel
      });

      cnt[place]++;
    };

    var cumulativeNodes = {
      top: null,
      right: null,
      bottom: null,
      left: null
    };

    var rel;
    var i = jsonObj.nodes.length;
    while (i--) { // looping backwards to splice array as I go
      var n = jsonObj.nodes[i];
      n.parentId = masterNode.id;
      if (rel = masterNode.childrenTop.find(function(x){ return(x.target === n.id);})) {
        addNewNode(n, 'top', i, [0, 2], rel, masterNode.childrenTop.length, true);
      }
      else if (rel = masterNode.childrenRight.find(function(x){ return(x.target === n.id);})) {
        addNewNode(n, 'right', i, [1, 3], rel, masterNode.childrenRight.length, false);
      }
      else if (rel = masterNode.childrenBottom.find(function(x){ return(x.target === n.id);})) {
        addNewNode(n, 'bottom', i, [2, 0], rel, masterNode.childrenBottom.length, true);
      }
      else if(rel = masterNode.childrenLeft.find(function(x){ return(x.target === n.id);})) {
        addNewNode(n, 'left', i, [3, 1], rel, masterNode.childrenLeft.length, false);
      }

      // TODO need to take care of other relations
      // not checking if other nodes already created edge
      // addEdgesIfApplicable(n, n.childrenTop, [0, 2]);
      // addEdgesIfApplicable(n, n.childrenRight, [1, 3]);
      // addEdgesIfApplicable(n, n.childrenBottom, [2, 0]);
      // addEdgesIfApplicable(n, n.childrenLeft, [3, 1]);
      edges = edges.concat(thisGraph.getPossibleNonMasterEdgesJson(n, n.childrenTop, [0,2], jsonObj.nodes, edges));
      edges = edges.concat(thisGraph.getPossibleNonMasterEdgesJson(n, n.childrenRight, [1,3], jsonObj.nodes, edges));
      edges = edges.concat(thisGraph.getPossibleNonMasterEdgesJson(n, n.childrenBottom, [2,0], jsonObj.nodes, edges));
      edges = edges.concat(thisGraph.getPossibleNonMasterEdgesJson(n, n.childrenLeft, [3,1], jsonObj.nodes, edges));
    }

    jsonObj['edges'] = edges;
    return {"json": jsonObj, "masterJson": masterNode, "cumulative": cumulativeNodes};
  };

  GraphCreator.prototype.createCumulativeNodes = function(cumulativeNodes){
    var result = [];

    if(cumulativeNodes.top) result.push(cumulativeNodes.top);
    if(cumulativeNodes.right) result.push(cumulativeNodes.right);
    if(cumulativeNodes.bottom) result.push(cumulativeNodes.bottom);
    if(cumulativeNodes.left) result.push(cumulativeNodes.left);
    return result;
  };


  GraphCreator.prototype.initializeEdge = function(edgeJson){
    var thisGraph = this;
    var src =  thisGraph.nodes.filter(function(n) {
      return n.id === edgeJson.source;
    })[0];
    var targ = thisGraph.nodes.filter(function(n) {
      return n.id === edgeJson.target;
    })[0];
    if (src === undefined || targ === undefined){
      console.log("error - node undefined, scr: " + src + ", targ: " + targ);
      return null;
    }
    var edgeDetails = thisGraph.relations.getEdgeDetails(edgeJson.rel);
    return {
      source: src,
      target: targ,
      connectionPoints: edgeJson.connectedTo,
      pathTextSrc: edgeDetails.textSrc,
      pathTextTarget: edgeDetails.textTarget,
      color: edgeDetails.color,
      dotted: edgeDetails.dotted
    };
  };

  GraphCreator.prototype.updateWithNewNodes = function(jsonObj, masterNode, cumulativeNodes){
      var thisGraph = this;
      // try {
        var newNodes = [];

        for(var i = 0; i < jsonObj.nodes.length; i++){
          if(!thisGraph.nodes.find(function(n){ return(n.id === jsonObj.nodes[i].id);})){
            newNodes.push(new GraphNode(jsonObj.nodes[i], masterNode));
          }
        }

        var cumulativeNodesArr = thisGraph.createCumulativeNodes(cumulativeNodes);

        newNodes = newNodes.concat(cumulativeNodesArr); //thisGraph.createCumulativeNodes(cumulativeNodes));

        thisGraph.nodes = thisGraph.nodes.concat(newNodes);
        masterNode.convertChildrenToRef(newNodes);
        masterNode.addCumulativeChildrenRef(cumulativeNodes);
        thisGraph.setIdCt += jsonObj.nodes.length;

        var newEdges = [];
        for(var i =0; i< jsonObj.edges.length; i++){
          var e = jsonObj.edges[i];
          var newEdge = thisGraph.initializeEdge(e);
          if(newEdge !== null){
            newEdges.push(newEdge);
          }
          continue;
          var edgeDetails = thisGraph.relations.getEdgeDetails(e.rel);
          var src =  thisGraph.nodes.filter(function(n) {
            return n.id === e.source;
          })[0];
          var targ = thisGraph.nodes.filter(function(n) {
            return n.id === e.target;
          })[0];
          if (src === undefined || targ === undefined){
            console.log("error - node undefined, scr: " + src + ", targ: " + targ);
            continue;
          }
          newEdges.push({
            source: thisGraph.nodes.filter(function(n) {
              return n.id === e.source;
            })[0],
            target: thisGraph.nodes.filter(function(n) {
              return n.id === e.target;
            })[0],
            connectionPoints: e.connectedTo,
            rel: e.rel,
            // todo get relation type at this point
            pathTextSrc: edgeDetails.textSrc,
            pathTextTarget: edgeDetails.textTarget,
            color: edgeDetails.color,
            dotted: edgeDetails.dotted
          });
        }
        thisGraph.edges = thisGraph.edges.concat(newEdges);
        thisGraph.updateGraph();
      // }
      // catch (err) {
      //   window.alert("Error parsing json\nerror message: " + err.message);
      // }
  };

  GraphCreator.prototype.setIdCt = function(idct) {
    this.idct = idct;
  };

  GraphCreator.prototype.consts = {
    selectedClass: "selected",
    connectClass: "connect-node",
    boatGClass: "conceptG",
    graphClass: "graph",
    activeEditId: "active-editing",
    BACKSPACE_KEY: 8,
    DELETE_KEY: 46,
    ENTER_KEY: 13,
    CTRL_KEY: 17,
    nodeRadius: 50,
    nodeDistance: 225,
    nodeHeight: 35,
    nodeWidth: 50,
    maxDefaultNodesVertically: 5,
    maxDefaultNodesHorizontally: 5
  };

  GraphCreator.prototype.showBrush = function(){
    var thisGraph = this;
    thisGraph.state.brushSelect = true;
    if(!thisGraph.brushHandle){
      thisGraph.brushHandle =  thisGraph.svg.append("g")
        .attr("class", "brush")
        .call(thisGraph.brush);
    }
  };

  GraphCreator.prototype.destroyBrush = function(){
    var thisGraph = this;
    if(thisGraph.brushHandle){
      thisGraph.brushHandle.remove();
      thisGraph.brushHandle = null;
    }

  };

  GraphCreator.prototype.exitBrushMode = function(){
    var thisGraph = this;
    if(thisGraph.state.brushSelect){
      thisGraph.unmarkAllNodes();
      thisGraph.state.brushSelect = false;
    }
  };

  GraphCreator.prototype.unmarkAllNodes = function(){
    var thisGraph = this;
    for(var i =0; i < thisGraph.nodes.length; i++){
      thisGraph.nodes[i].unmarkSelected();
    }
    thisGraph.updateGraph();
  };

  GraphCreator.prototype.getPointInvertedTranslation = function(origin, translate, scale){
    return (origin - translate * (1 - scale))/scale - translate;
  };

  GraphCreator.prototype.getPointTransform = function(origin, translate, scale){
    return ((origin + translate) * scale + translate *(1-scale));
  };

  GraphCreator.prototype.getPointTransformX = function(originalX){
    var thisGraph = this;
    return thisGraph.getPointTransform(originalX, thisGraph.transformed.x, thisGraph.transformed.k);
  };
  GraphCreator.prototype.getPointTransformY = function(originalY){
    var thisGraph = this;
    return thisGraph.getPointTransform(originalY, thisGraph.transformed.y, thisGraph.transformed.k);
  };


  GraphCreator.prototype.markNodesSelectedInRange = function(range){
    var thisGraph = this;
    var x1 = range[0][0],
      x2 = range[1][0],
      y1 = range[0][1],
      y2 = range[1][1];


    var xTrans = thisGraph.transformed.x;
    var yTrans = thisGraph.transformed.y;
    var s = thisGraph.transformed.k;

    x1 = thisGraph.getPointInvertedTranslation(x1, xTrans, s);
    x2 = thisGraph.getPointInvertedTranslation(x2, xTrans, s);

    y1 = thisGraph.getPointInvertedTranslation(y1, yTrans, s);
    y2 = thisGraph.getPointInvertedTranslation(y2, yTrans, s);


    for(var i=0; i < thisGraph.nodes.length; i++){
      var node = thisGraph.nodes[i];
      if( node.x > x1 && node.x < x2 && node.y > y1 && node.y < y2){
        node.markSelected();
      } else{
        node.unmarkSelected();
      }
    }
    thisGraph.updateGraph();
  };

  GraphCreator.prototype.brushed = function(){
    var thisGraph = this;

    var s = d3.event.selection;
    if (s === null) {
      thisGraph.unmarkAllNodes();

    } else {
      thisGraph.markNodesSelectedInRange(s);
    }

  };
  /* PROTOTYPE FUNCTIONS */

  GraphCreator.prototype.dragmove = function(d) {
    var thisGraph = this;

    if (thisGraph.state.shiftNodeDrag) {
      thisGraph.dragLine.attr('d', 'M' + d.x + ',' + d.y + 'L' + d3.mouse(thisGraph.svgG.node())[0] + ',' + d3.mouse(this.svgG.node())[1]);
    }
    else {
    if(!d.selected){ // if not selected node, reset state
      thisGraph.exitBrushMode();
    }
    if (!thisGraph.state.brushSelect) {
      d.x += d3.event.dx;
      d.y += d3.event.dy;
    } else{
       for (var i = 0; i < thisGraph.nodes.length; i++){
         if (thisGraph.nodes[i].selected){
           thisGraph.nodes[i].x += d3.event.dx;
           thisGraph.nodes[i].y += d3.event.dy;
         }
       }
     }
     thisGraph.updateGraph();
    }
  };

  GraphCreator.prototype.deleteGraph = function(skipPrompt) {
    var thisGraph = this,
      doDelete = true;
    if (!skipPrompt) {
      doDelete = window.confirm("Press OK to delete this graph");
    }
    if (doDelete) {
      thisGraph.nodes = [];
      thisGraph.edges = [];
      thisGraph.updateGraph();
    }
  };

  /* select all text in element: taken from http://stackoverflow.com/questions/6139107/programatically-select-text-in-a-contenteditable-html-element */
  GraphCreator.prototype.selectElementContents = function(el) {
    var range = document.createRange();
    range.selectNodeContents(el);
    var sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(range);
  };

  GraphCreator.prototype.getNodeTitle = function(gEl, title) {
    gEl.append("text")
    .attr("text-anchor", "middle")
    .attr("font-size", "10px")
    .text(title.length < 18 ? title : String(title).slice(0,15) + "...")
    .attr("dy", "3");
  };


  // remove edges associated with a node
  GraphCreator.prototype.spliceLinksForNode = function(node) {
    var thisGraph = this,
      toSplice = thisGraph.edges.filter(function(l) {
        return (l.source === node || l.target === node);
      });
    toSplice.map(function(l) {
      thisGraph.edges.splice(thisGraph.edges.indexOf(l), 1);
    });
  };

  GraphCreator.prototype.replaceSelectEdge = function(d3Path, edgeData) {
    var thisGraph = this;
    d3Path.classed(thisGraph.consts.selectedClass, true);
    if (thisGraph.state.selectedEdge) {
      thisGraph.removeSelectFromEdge();
    }
    thisGraph.state.selectedEdge = edgeData;
  };

  GraphCreator.prototype.replaceSelectNode = function(d3Node, nodeData) {
    var thisGraph = this;
    d3Node.classed(this.consts.selectedClass, true);
    if (thisGraph.state.selectedNode) {
      thisGraph.removeSelectFromNode();
    }
    thisGraph.state.selectedNode = nodeData;
  };

  GraphCreator.prototype.removeSelectFromNode = function() {
    var thisGraph = this;
    thisGraph.boats.filter(function(cd) {
      return cd.id === thisGraph.state.selectedNode.id;
    }).classed(thisGraph.consts.selectedClass, false);
    thisGraph.state.selectedNode = null;
  };

  GraphCreator.prototype.removeSelectFromEdge = function() {
    var thisGraph = this;
    thisGraph.paths.filter(function(cd) {
      return cd === thisGraph.state.selectedEdge;
    }).classed(thisGraph.consts.selectedClass, false);
    thisGraph.state.selectedEdge = null;
  };


  // mousedown on main svg
  GraphCreator.prototype.svgMouseDown = function() {
    this.state.graphMouseDown = true;
    this.state.brushSelect = false;
    this.sensesList.style('display', 'none');
    this.unmarkAllNodes();
  };

  // mouseup on main svg
  GraphCreator.prototype.svgMouseUp = function() {
    var thisGraph = this,
      state = thisGraph.state;
    if (state.justScaleTransGraph) {
      // dragged not clicked
      state.justScaleTransGraph = false;
    }

    else if (state.graphMouseDown && d3.event.shiftKey) {
      // clicked not dragged from svg
      var xycoords = d3.mouse(thisGraph.svgG.node()),
        d = {
          id: thisGraph.idct++,
          title: consts.defaultTitle,
          x: xycoords[0],
          y: xycoords[1]
        };
      thisGraph.nodes.push(d);
      thisGraph.updateGraph();
    }
    else if (state.shiftNodeDrag) {
      // dragged from node
      state.shiftNodeDrag = false;
      thisGraph.dragLine.classed("hidden", true);
    }
    state.graphMouseDown = false;
  };

  // keydown on main svg
  GraphCreator.prototype.svgKeyDown = function() {
    var thisGraph = this,
      state = thisGraph.state,
      consts = thisGraph.consts;
    // make sure repeated key presses don't register for each keydown
    if (state.lastKeyDown !== -1) return;

    state.lastKeyDown = d3.event.keyCode;
    var selectedNode = state.selectedNode,
      selectedEdge = state.selectedEdge;

    switch (d3.event.keyCode) {
      case consts.BACKSPACE_KEY:
        break;
      case consts.CTRL_KEY:
        thisGraph.showBrush();
        break;
      case consts.DELETE_KEY:
        if (selectedNode) {
          thisGraph.nodes.splice(thisGraph.nodes.indexOf(selectedNode), 1);
          thisGraph.spliceLinksForNode(selectedNode);
          state.selectedNode = null;
          thisGraph.updateGraph();
        }
        else if (selectedEdge) {
          thisGraph.edges.splice(thisGraph.edges.indexOf(selectedEdge), 1);
          state.selectedEdge = null;
          thisGraph.updateGraph();
        }
        break;
    }
  };

  GraphCreator.prototype.svgKeyUp = function() {
    var thisGraph = this;
    thisGraph.destroyBrush();
    this.state.lastKeyDown = -1;
  };

  GraphCreator.prototype.removeNodeEdges = function(node){
    var thisGraph = this;
    var i = thisGraph.edges.length;

    while(i--){
      var edge = thisGraph.edges[i];
      if(edge.source === node || edge.target === node){
        thisGraph.edges.splice(i, 1);
      }
    }
  };

  GraphCreator.prototype.removeNode = function (node) {
    var thisGraph = this;
    // first remove edges
    thisGraph.removeNodeEdges(node);
    // remove children
    var children = node.getAllChildren(node);

    for(var i=0; i < children.length; i++){
      thisGraph.removeNode(children[i]);  // removing children recursively
    }
    // remove itself
    thisGraph.nodes.splice(thisGraph.nodes.indexOf(node), 1);
  };

  GraphCreator.prototype.removeNodeChildren = function(node, place){
    var thisGraph = this;
    var toRemove;
    if(place !== undefined){ // remove at specific position
      toRemove = node.getChildrenAtPosition(place);
    } else{ // remove all children
      toRemove = node.getAllChildren();
    }
    for(var i=0; i < toRemove.length; i++){
      thisGraph.removeNode(toRemove[i]);
    }
    node.clearChildrenRef(place);
    thisGraph.updateGraph();
  };

  // call to propagate changes to graph
  GraphCreator.prototype.updateGraph = function() {
    var thisGraph = this,
      consts = thisGraph.consts,
      state = thisGraph.state;
    // update existing nodes
    // thisGraph.boats = thisGraph.boats.data(thisGraph.nodes, function(d) {
    thisGraph.boats = d3.select("#all-boats-id").selectAll("g").data(thisGraph.nodes, function(d) {
      return d.id;
    });
    thisGraph.boats
      .attr("transform", function(d) {
      return "translate(" + d.x + "," + d.y + ")";
      })
      .classed("node-selected",function(d){
        return d.selected;
      })
      .on("mouseover", function(d) {
      var x = d.x;
      var y = d.y;
      var t = thisGraph.transformed;
      if(d.title.length >= 18){
        thisGraph.tooltip.transition()
          .duration(100)
          .style("opacity", .9);
        thisGraph.tooltip.html(d.title)
          .style("left", thisGraph.getPointTransformX(x) + t.k * 70 + "px")
          .style("top", thisGraph.getPointTransformY(y) - 15 + "px");
      }
      })
      .on("mouseout", function(d) {
        thisGraph.tooltip.transition()
          .duration(200)
          .style("opacity", 0);
      });

    // add new nodes
    var newGs = thisGraph.boats.enter()
      .append("g");

    newGs.classed(consts.boatGClass, true)
      .attr("transform", function(d) {

        return "translate(" + d.x + "," + d.y + ")";
      })
      .call(thisGraph.drag);

    newGs.append("polyline")
      .classed("inner-node", true)
      .attr("points", function(d){
        var radius = 30;
        return d.getType() === "ordinary" ?
        "-50,10 -10,15 10,15 50,10 50,-10 10,-15 -10,-15 -50,-10 -50,10" :
        "-30,0 0,-30 30,0 0,30 -30,0";
      })
      // .attr("points", "-50,15 -15,25 15,10 50,15 50,-15 15,-20 -15,-20 -50,-15 -50,15")
      .attr("stroke-width", "1.5px")
      .attr("fill", function(d){
        return d.type.color || "red";
      })
      .attr("stroke", "black")
      .on('contextmenu', function(d){
        d3.event.preventDefault();
          if (d.getType() === 'cumulator') {
            thisGraph.lastClickedNodeCumulator = d;
            var hiddenNodes = d.getHiddenListHtml();
            thisGraph.hiddenList
              .style("left", thisGraph.transformed.x + (d.x +30)* thisGraph.transformed.k + "px")
              .style("top", thisGraph.transformed.y + (d.y + 15)  * thisGraph.transformed.k + "px")
              .style("display", "block")
              .html(hiddenNodes.html);
            d.addOnclickToCreatedNodes(thisGraph, hiddenNodes.createdIds, hiddenNodes.hiddenRef);
          }
        })
      .on('mousedown', function(d){
        if (d.getType() === 'ordinary'){
          thisGraph.eventDispatch.call("ordinary-node-mousedown", d, {nodeId: d.id});
        }
      });

    // TODO append 4 action buttons
    newGs.each(function(d){
      thisGraph.appendChildrenButtons(d3.select(this), d);
    });

    newGs.each(function(d) {
      thisGraph.getNodeTitle(d3.select(this), d.title);
    });

    // remove old nodes
    thisGraph.boats.exit().remove();



    var paths = d3.select("#all-paths-id").selectAll("path").data(thisGraph.edges, function(d){
      if(d)
        return d; //String(d.source.id) + "+" + String(d.target.id);
      return d;
    })
      .classed(consts.selectedClass, function(d) {
        return d === state.selectedEdge;
      })
      .attr("d", function(d) {
        return "M" + d.source.x + "," + d.source.y + "L" + d.target.x + "," + d.target.y;
      });

    paths.exit().remove(); // EXIT

    paths.enter().append("path") // ENTER
      .merge(paths) // ENTER + UPDATE
      .style('marker-end', 'url(#end-arrow)')
      .style('marker-start', 'url(#start-arrow)')
      .classed("link", true)
      .classed("dotted", function(d){
        return d.dotted;
      })
      .attr("stroke", function (d) {
        return d.color;
      })
      .attr("d", function(d) {
        var connectionPoints = thisGraph.getConnectionPoints(d);
        if(d.target.isNodeCumulator()){
          return "M" + connectionPoints.source.x + "," + connectionPoints.source.y + "L" + d.target.x + "," + d.target.y;
        }
        return "M" + connectionPoints.source.x + "," + connectionPoints.source.y + "L" + connectionPoints.target.x + "," + connectionPoints.target.y;
      })
      .attr("id", function (d,i) { return "path_" + String(d.source.id)+ '-' + String(d.target.id); });


    var pathsText = d3.select("#all-paths-text-id").selectAll("text").data(thisGraph.edges, function(d, i){
      return String(d.source.id)+ '-' + String(d.target.id);
    });

    var text = pathsText.enter()
        .append("text")
        .classed("relation-text", true)
        .attr("dy", -5);

    text.exit().remove();

    text
        .append("textPath")
        .attr("startOffset", "5%")
        .attr("xlink:href", function (d) { return "#path_" + String(d.source.id)+ '-' + String(d.target.id); })
        .text(function (d) { return d.pathTextSrc  }); // + " "+ d.rel;});

    text
       .append("textPath")
       .attr("startOffset", "75%")
       .attr("xlink:href", function (d) { return "#path_" + String(d.source.id)+ '-' + String(d.target.id); })
       .text(function (d) { return d.pathTextTarget; });

    pathsText.exit().remove();
  };

  GraphCreator.prototype.appendChildrenButtons = function(nodeHandle, d){
    var thisGraph = this;

    var expandingNodeParent = thisGraph.nodes.filter(function(n) {
      return n.id === d.parent;
    })[0];

    if(d.childrenTop.length > 0){
      var triangleTop = nodeHandle.append("polyline")
        .attr("points", '-13 -15, 13 -15, 0 -5, -13 -15')
        .attr("stroke-width", "1.5px")
        .attr("fill", "blue")
        .attr("stroke", "black")
        .classed("expanded", function(d){ return d.expandedTop})
        .on("click", function(){
          d.expandTriangleClick(thisGraph, this, 0);
        });
    }
    if(d.childrenRight.length > 0) {
      var shapeRight = nodeHandle.append("polyline")
        .attr("points", "50,10 40 0, 50 -10")
        .attr("stroke-width", "1.5px")
        .attr("fill", "blue")
        .attr("stroke", "black")
        .classed("expanded", function(d){ return d.expandedRight})
        .on("click", function () {
          d.expandTriangleClick(thisGraph, this, 1);
        });
    }
    if(d.childrenBottom.length > 0) {
      var triangleBottom = nodeHandle.append("polyline")
        .attr("points", '-13 15, 13 15, 0 5, -13 15')
        .attr("stroke-width", "1.5px")
        .attr("fill", "blue")
        .attr("stroke", "black")
        .classed("expanded", function(d){ return d.expandedBottom})
        .on("click", function () {
          d.expandTriangleClick(thisGraph, this, 2);
        });
    }
    if(d.childrenLeft.length > 0) {
      var shapeLeft = nodeHandle.append("polyline")
        .attr("points", "-50,10 -40 0, -50 -10")
        .attr("stroke-width", "1.5px")
        .attr("fill", "blue")
        .attr("stroke", "black")
        .classed("expanded", function(d){ return d.expandedLeft})
        .on("click", function () {
          d.expandTriangleClick(thisGraph, this, 3);
        });
    }
  };
  GraphCreator.prototype.expandNode = function(d, newExpandedNodesData, interestingNodes){
    var thisGraph = this;
    if(newExpandedNodesData){
        // d.addNewConnections(newExpandedNodesData);
        var prePrepare = thisGraph.prePrepareJsonApi(newExpandedNodesData, d.id, interestingNodes);
        var result = thisGraph.prepareJsonApi(prePrepare, false, d);
        thisGraph.updateWithNewNodes(result['json'], d, result['cumulative']);
    }
  };
  GraphCreator.prototype.getConnectionPoints = function(edge) {
    /*
     * array of len 2,
     * [0] -> connection point source
     * [1] -> connection point destination
     * 4 possible connection points
     * 0, 1, 2, 3 -> top, right, bottom, left
     */
    var thisGraph = this;
    try {
      return {
        source: thisGraph.getConnectionPoint(edge.connectionPoints[0], edge.source.x, edge.source.y),
        target: thisGraph.getConnectionPoint(edge.connectionPoints[1], edge.target.x, edge.target.y),
      }
    }
    catch (err){
      console.log(edge);
    }
  };

  GraphCreator.prototype.getConnectionPoint = function(mode, x, y) {
    var thisGraph = this;
    var ret = {
      x: x,
      y: y
    };
    switch (mode) {
      case 0:
        ret['y'] -= thisGraph.consts.nodeHeight / 2;
        break;
      case 1:
        ret['x'] += 50;
        break;
      case 2:
        ret['y'] += thisGraph.consts.nodeHeight / 2;
        break;
      case 3:
        ret['x'] -= 50;
        break;
      default:
    }
    return ret;
  };

  GraphCreator.prototype.zoomed = function() {
    this.state.justScaleTransGraph = true;
    d3.select("." + this.consts.graphClass)
      .attr("transform", d3.event.transform);
    this.transformed = d3.event.transform;
    this.hiddenList
      .style("display", "none");
  };

  GraphCreator.prototype.updateWindow = function(svg) {
    var docEl = document.documentElement,
      bodyEl = document.getElementsByTagName('body')[0];
    var x = window.innerWidth || docEl.clientWidth || bodyEl.clientWidth;
    var y = window.innerHeight || docEl.clientHeight || bodyEl.clientHeight;
    svg.attr("width", x).attr("height", y);
  };

  GraphCreator.prototype.prePrepareJsonApi = function(d, parentId, interestingNodes){
    var thisGraph = this;

    interestingNodes = interestingNodes || null; // if specified, only those will be returned

    var createdNodes = {
      masterNode: {},
      nodes: []
    };


    var alreadyIn = [];
    for(var i = 0; i< d.length; i++) {
      var synset = d[i]['synset'];
      var id = synset['id'];

      if (interestingNodes && !interestingNodes.find(function(x){return x['target']=== id}))
        continue;


      var senses = synset['senses'];
      var lemma = senses[Object.keys(senses)[0]]['lemma']['word'];
      var partOfSpeechId = senses[Object.keys(senses)[0]]['pos'];

      var childrenTop = [];
      var childrenRight = [];
      var childrenBottom = [];
      var childrenLeft = [];

      // sorting relations by order ???
      // possibly needed if double relation between nodes exists with different
      // connection points
      // thisGraph.relations.sortRelations(d[i]['relsFrom']);

      for (var j = 0; j < d[i]['relsFrom'].length; j++) {
        var rel = d[i]['relsFrom'][j];
        if(rel.target === parentId)
          continue;
        var type = thisGraph.relations.getType(rel.rel);

        switch (type) {
          case 0:
            childrenTop.push({target: rel.target, rel: rel.rel});
            break;
          case 1:
            childrenRight.push({target: rel.target, rel: rel.rel});
            break;
          case 2:
            childrenBottom.push({target: rel.target, rel: rel.rel});
            break;
          case 3:
            childrenLeft.push({target: rel.target, rel: rel.rel});
            break;
        }
      }

      var comparator = function(x){
        return x.target === rel.target;
      };

      for (j = 0; j < d[i]['relsTo'].length; j++){
        var rel = d[i]['relsTo'][j];
        if(rel.target === parentId)
          continue;

        // error it is possible that double relation is assigning different places!!!
        // that is why it is necessary to check every previously inserted edge
        if(childrenTop.find(comparator) ||
          childrenRight.find(comparator)  ||
          childrenBottom.find(comparator)  ||
          childrenLeft.find(comparator))
          continue;

        type = thisGraph.relations.getType(rel.rel);

        switch (type) {
          case 0:
            childrenTop.push({target: rel.target, rel: rel.rel});
            break;
          case 1:
            childrenRight.push({target: rel.target, rel: rel.rel});
            break;
          case 2:
            childrenBottom.push({target: rel.target, rel: rel.rel});
            break;
          case 3:
            childrenLeft.push({target: rel.target, rel: rel.rel});
            break;
        }
      }


      var node = {
        id: id,
        title: lemma,
        partOfSpeechId: partOfSpeechId,
        childrenTop: childrenTop,
        childrenRight: childrenRight,
        childrenBottom: childrenBottom,
        childrenLeft:childrenLeft
      };
      if(alreadyIn.indexOf(id) === -1) {
        if (parentId === id)
          createdNodes.masterNode = node;
        else
          createdNodes.nodes.push(node);
        alreadyIn.push(id);
      }
    }
    return createdNodes;

  };

  GraphCreator.prototype.initializeFromSynsetId = function(id){
    var thisGraph = this;
    var parentId = id;
    var callback = function(d){
      var createdNodes = thisGraph.prePrepareJsonApi(d, parentId);
      var result = thisGraph.prepareJsonApi(createdNodes, true, createdNodes.masterNode);
      createdNodes = result["json"];
      var cumulativeNodes = result["cumulative"];
      var masterNodeJson = result["masterJson"];
      thisGraph.initFromJson(createdNodes, masterNodeJson, cumulativeNodes);
    };
    thisGraph.api.getGraph(id, function(d){
      callback(d);
    });
  };

  // for lexical unit
  GraphCreator.prototype.initializeFromSenseId = function(id){
    var thisGraph = this;
    var parentId = id;
    var callback = function(d){
      console.log(d);
      var createdNodes = thisGraph.prePrepareJsonApi(d, parentId);
      var result = thisGraph.prepareJsonApi(createdNodes, true, createdNodes.masterNode);
      createdNodes = result["json"];
      var cumulativeNodes = result["cumulative"];
      var masterNodeJson = result["masterJson"];
      thisGraph.initFromJson(createdNodes, masterNodeJson, cumulativeNodes);
    };
    thisGraph.api.getSenseGraph(id, function(d){
      callback(d);
    });
  };

  /* exporting picture
   * reference Nikita Rokotyan - http://bl.ocks.org/Rokotyan/0556f8facbaf344507cdc45dc3622177
   * using FileSaver.js library & Canvas-to-Blob.js
   */
  GraphCreator.prototype.exportVisualization = function(){
    var thisGraph = this;
    var svgString = thisGraph.getSVGString(thisGraph.svg.node());
    thisGraph.svgString2Image( svgString, 2*thisGraph.width, 2*thisGraph.height, 'png', save ); // passes Blob and filesize String to the callback

    function save( dataBlob, filesize ){
      saveAs( dataBlob, 'wordnet-graph.png' ); // FileSaver.js function
    }
  };

  // Below are the functions that handle actual exporting:
  // getSVGString ( svgNode ) and svgString2Image( svgString, width, height, format, callback )
  GraphCreator.prototype.getSVGString = function(svgNode) {
    svgNode.setAttribute('xlink', 'http://www.w3.org/1999/xlink');
    var cssStyleText = getCSSStyles( svgNode );
    appendCSS( cssStyleText, svgNode );

    var serializer = new XMLSerializer();
    var svgString = serializer.serializeToString(svgNode);
    svgString = svgString.replace(/(\w+)?:?xlink=/g, 'xmlns:xlink='); // Fix root xlink without namespace
    svgString = svgString.replace(/NS\d+:href/g, 'xlink:href'); // Safari NS namespace fix

    return svgString;

    function getCSSStyles( parentElement ) {
      var selectorTextArr = [];

      // Add Parent element Id and Classes to the list
      selectorTextArr.push( '#'+parentElement.id );
      for (var c = 0; c < parentElement.classList.length; c++)
        if ( !contains('.'+parentElement.classList[c], selectorTextArr) )
          selectorTextArr.push( '.'+parentElement.classList[c] );

      // Add Children element Ids and Classes to the list
      var nodes = parentElement.getElementsByTagName("*");
      for (var i = 0; i < nodes.length; i++) {
        var id = nodes[i].id;
        if ( !contains('#'+id, selectorTextArr) )
          selectorTextArr.push( '#'+id );

        var classes = nodes[i].classList;
        for (var c = 0; c < classes.length; c++)
          if ( !contains('.'+classes[c], selectorTextArr) )
            selectorTextArr.push( '.'+classes[c] );
      }

      // Extract CSS Rules
      var extractedCSSText = "";
      for (var i = 0; i < document.styleSheets.length; i++) {
        var s = document.styleSheets[i];

        try {
          if(!s.cssRules) continue;
        } catch( e ) {
          if(e.name !== 'SecurityError') throw e; // for Firefox
          continue;
        }

        var cssRules = s.cssRules;
        for (var r = 0; r < cssRules.length; r++) {
          if ( contains( cssRules[r].selectorText, selectorTextArr ) )
            extractedCSSText += cssRules[r].cssText;
        }
      }
      return extractedCSSText;

      function contains(str,arr) {
        return arr.indexOf( str ) === -1 ? false : true;
      }
    }

    function appendCSS( cssText, element ) {
      var styleElement = document.createElement("style");
      styleElement.setAttribute("type","text/css");
      styleElement.innerHTML = cssText;
      var refNode = element.hasChildNodes() ? element.children[0] : null;
      element.insertBefore( styleElement, refNode );
    }
  }

  GraphCreator.prototype.svgString2Image = function( svgString, width, height, format, callback ) {
    var format = format ? format : 'png';

    var imgsrc = 'data:image/svg+xml;base64,'+ btoa( unescape( encodeURIComponent( svgString ) ) ); // Convert SVG string to data URL

    var canvas = document.createElement("canvas");
    var context = canvas.getContext("2d");

    canvas.width = width;
    canvas.height = height;

    var image = new Image();
    image.onload = function() {
      context.clearRect ( 0, 0, width, height );
      context.drawImage(image, 0, 0, width, height);

      canvas.toBlob( function(blob) {
        var filesize = Math.round( blob.length/1024 ) + ' KB';
        if ( callback ) callback( blob, filesize );
      });
    };
    image.src = imgsrc;
  }

  GraphCreator.prototype.resizeSVG = function (width, height) {
    var self = this;
    self.width = width;
    self.height = height;
    self.svg.attr("width", self.width).attr("height", self.height);
  };






