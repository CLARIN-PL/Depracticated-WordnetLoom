"use strict";

var ApiConnector = function(){
  var self = this;
  self.url = "http://api.slowosiec.clarin-pl.eu/plwordnet-api/";
  // self.url = 'http://156.17.135.29:8080/yiddish/api/'
};

ApiConnector.prototype.getGraph = function(id, callback){
  var self = this;
  d3.json(self.url + "synsets/" + id + "/cache", function(status, json){
    if(json)
      callback(json);
    else
      callback(null);
    });
};

ApiConnector.prototype.getSenseGraph = function(id, callback){
  var self = this;
  d3.json(self.url + "sense/" + id + "/graph", function(status, json){
    if(json)
      callback(json);
    else
      callback(null);
  });
};

ApiConnector.prototype.getSynsetFromSenseId = function(id, given_callback){
  var self = this;

  var inner_callback = function (d) {
    given_callback(d)
  };

  d3.json(self.url + "senses/"+id+"/synset", function(status, json){
    if(json){
      inner_callback(json);
    } else {
      inner_callback(null);
    }
  });
};


ApiConnector.prototype.getSensesFromLemma = function(lemma, callback){
  var self = this;
  d3.json(self.url + "synsets/search?lemma=" + lemma, function (status, json) {
    if(json){
      callback(json);
    } else {
      callback(null);
    }
  });
};
/*
d3.json("http://api.slowosiec.clarin-pl.eu/plwordnet-api/synsets/123/cachefsdf", function (error, json) {
  if(json)
    return json;
});

  */
