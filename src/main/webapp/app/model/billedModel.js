// Generated by CoffeeScript 1.12.7
(function() {
  var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  __Model.Billed = (function(superClass) {
    extend(Billed, superClass);

    function Billed() {
      return Billed.__super__.constructor.apply(this, arguments);
    }

    Billed.fields("bilId", "bilRate", "bilTaskId");

    return Billed;

  })(__Model.General);

}).call(this);
