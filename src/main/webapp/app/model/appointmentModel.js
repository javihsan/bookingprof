// Generated by CoffeeScript 1.12.7
(function() {
  var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  __Model.Appointment = (function(superClass) {
    extend(Appointment, superClass);

    function Appointment() {
      return Appointment.__super__.constructor.apply(this, arguments);
    }

    Appointment.fields("apoId", "apoStartTime", "apoName", "apoX", "apoY", "apoColor", "apoCalendarId", "apoCalendarName", "bgColor");

    return Appointment;

  })(__Model.General);

}).call(this);
