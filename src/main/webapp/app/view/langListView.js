// Generated by CoffeeScript 1.12.7
(function() {
  var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  __View.LangListView = (function(superClass) {
    extend(LangListView, superClass);

    function LangListView() {
      return LangListView.__super__.constructor.apply(this, arguments);
    }

    LangListView.prototype.container = "section#booking article#langs-admin ul";

    LangListView.prototype.template_url = "/app/templates/langList.mustache";

    return LangListView;

  })(Monocle.View);

}).call(this);