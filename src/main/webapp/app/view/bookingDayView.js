// Generated by CoffeeScript 1.12.7
(function() {
  var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  __View.BookingDayView = (function(superClass) {
    extend(BookingDayView, superClass);

    function BookingDayView() {
      return BookingDayView.__super__.constructor.apply(this, arguments);
    }

    BookingDayView.prototype.container = "section#booking article#table-day div#table-day-container";

    BookingDayView.prototype.template_url = "/app/templates/bookingDay.mustache";

    BookingDayView.prototype.events = {
      "singleTap div": "onTap"
    };

    BookingDayView.prototype.onTap = function(event) {
      var calendarSel, selectedCalendars;
      __FacadeCore.Cache_remove(appName + "newApo");
      __FacadeCore.Cache_set(appName + "newApo", this.model);
      if (this.model.apoCalendarId !== null) {
        selectedCalendars = new Array();
        calendarSel = {
          id: this.model.apoCalendarId,
          name: this.model.apoCalendarName
        };
        selectedCalendars[0] = calendarSel;
        __FacadeCore.Cache_remove(appName + "selectedCalendars");
        __FacadeCore.Cache_set(appName + "selectedCalendars", selectedCalendars);
      }
      return __FacadeCore.Router_section("#newEvent");
    };

    return BookingDayView;

  })(Monocle.View);

}).call(this);
