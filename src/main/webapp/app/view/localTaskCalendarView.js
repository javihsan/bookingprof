// Generated by CoffeeScript 1.12.7
(function() {
  var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  __View.LocalTaskCalendarView = (function(superClass) {
    extend(LocalTaskCalendarView, superClass);

    function LocalTaskCalendarView() {
      return LocalTaskCalendarView.__super__.constructor.apply(this, arguments);
    }

    LocalTaskCalendarView.prototype.container = "section#localTaskCalendar article#localTaskCalendar-form ul";

    LocalTaskCalendarView.prototype.template_url = "/app/templates/localTaskCalendar.mustache";

    LocalTaskCalendarView.prototype.events = {
      "singleTap li": "onSelect"
    };

    LocalTaskCalendarView.prototype.onSelect = function(event) {
      this.model.updateAttributes({
        enabled: this.model.enabled ? false : true
      });
      return this.refresh();
    };

    return LocalTaskCalendarView;

  })(Monocle.View);

}).call(this);
