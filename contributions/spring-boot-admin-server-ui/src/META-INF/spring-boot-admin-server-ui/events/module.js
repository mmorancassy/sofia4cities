webpackJsonp([14],[function(t,e,n){(function(t){"use strict";var e=n(1),r=e.module("sba-events",["sba-core"]);t.sbaModules.push(r.name),r.controller("eventsCtrl",["$scope","$http",function(t,e){e.get("api/journal").then(function(e){t.journal=e.data}).catch(function(e){t.error=e.data})}]),r.config(["$stateProvider",function(t){t.state("events",{name:"events",url:"/events",templateUrl:"events/events.html",controller:"eventsCtrl"})}]),r.run(["MainViews",function(t){t.register({title:"Journal",state:"events",order:100})}])}).call(e,function(){return this}())}]);