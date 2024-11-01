// Generated by CoffeeScript 1.12.7
(function() {
  var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  __View.InvoiceListFromView = (function(superClass) {
    extend(InvoiceListFromView, superClass);

    function InvoiceListFromView() {
      return InvoiceListFromView.__super__.constructor.apply(this, arguments);
    }

    InvoiceListFromView.prototype.container = "section#booking article#list-invoices ul";

    InvoiceListFromView.prototype.template_url = "/app/templates/eventsListFrom.mustache";

    InvoiceListFromView.prototype.events = {
      "singleTap li": "onTap"
    };

    InvoiceListFromView.prototype.onTap = function(event) {
      return __Controller.InvoiceList.onPrevius(event);
    };

    return InvoiceListFromView;

  })(Monocle.View);

}).call(this);
