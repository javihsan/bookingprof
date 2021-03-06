// Generated by CoffeeScript 1.12.7
(function() {
  var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  __View.LangSelectView = (function(superClass) {
    extend(LangSelectView, superClass);

    function LangSelectView() {
      return LangSelectView.__super__.constructor.apply(this, arguments);
    }

    LangSelectView.prototype.container = "section#booking article#langs ul";

    LangSelectView.prototype.template_url = "/app/templates/langSelect.mustache";

    return LangSelectView;

  })(Monocle.View);

}).call(this);
