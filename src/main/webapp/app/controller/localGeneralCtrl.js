// Generated by CoffeeScript 1.12.7
(function() {
  var LocalGeneralCtrl,
    extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  LocalGeneralCtrl = (function(superClass) {
    extend(LocalGeneralCtrl, superClass);

    function LocalGeneralCtrl() {
      return LocalGeneralCtrl.__super__.constructor.apply(this, arguments);
    }

    LocalGeneralCtrl.prototype.events = {
      "singleTap a[data-action=save]": "onSave",
      "singleTap a[data-action=reset]": "onReset",
      "load #local-general": "loadGeneral"
    };

    LocalGeneralCtrl.prototype.elements = {
      "#local-general": "artLocalGeneral",
      "header a[href=\\#]": "header",
      "footer a": "footer",
      "a[data-action=reset]": "buttonReset",
      "a[data-action=save]": "buttonSave",
      "#locBookingClient": "locBookingClient",
      "#locName": "locName",
      "#error_locName": "error_locName",
      "#locAddress": "locAddress",
      "#error_locAddress": "error_locAddress",
      "#locCity": "locCity",
      "#error_locCity": "error_locCity",
      "#locState": "locState",
      "#error_locState": "error_locState",
      "#locCP": "locCP",
      "#error_locCP": "error_locCP",
      "#locResponName": "locResponName",
      "#error_locResponName": "error_locResponName",
      "#locResponSurname": "locResponSurname",
      "#error_locResponSurname": "error_locResponSurname",
      "#locResponEmail": "locResponEmail",
      "#error_locResponEmail": "error_locResponEmail",
      "#locResponTelf1": "locResponTelf1",
      "#error_locResponTelf1": "error_locResponTelf1",
      "#locMailBookign": "locMailBookign",
      "#locApoDuration": "locApoDuration",
      "#error_locApoDuration": "error_locApoDuration",
      "#locTimeRestricted": "locTimeRestricted",
      "#error_locTimeRestricted": "error_locTimeRestricted",
      "#locOpenDays": "locOpenDays",
      "#error_locOpenDays": "error_locOpenDays",
      "#locNumPersonsApo": "locNumPersonsApo",
      "#error_locNumPersonsApo": "error_locNumPersonsApo",
      "#locNumUsuDays": "locNumUsuDays",
      "#error_locNumUsuDays": "error_locNumUsuDays",
      "#locNewClientDefault": "locNewClientDefault",
      "#locMulServices": "locMulServices",
      "#locSelCalendar": "locSelCalendar"
    };

    LocalGeneralCtrl.prototype.onReset = function(event) {
      if (this.artLocalGeneral.hasClass("active")) {
        return this.loadGeneral(event);
      }
    };

    LocalGeneralCtrl.prototype.loadGeneral = function(event) {
      var firm, local;
      this.header.hide();
      this.footer.hide();
      this.buttonReset.show();
      this.buttonSave.show();
      local = __FacadeCore.Cache_get(appName + "local");
      this.locBookingClient[0].options.selectedIndex = local.locBookingClient;
      this.locName.val(local.locName);
      this.locAddress.val(local.locWhere.wheAddress);
      this.locCity.val(local.locWhere.wheCity);
      this.locState.val(local.locWhere.wheState);
      this.locCP.val(local.locWhere.wheCP);
      this.locResponName.val(local.locRespon.whoName);
      this.locResponSurname.val(local.locRespon.whoSurname);
      this.locResponEmail.val(local.locRespon.whoEmail);
      this.locResponTelf1.val(local.locRespon.whoTelf1);
      this.locApoDuration.val(local.locApoDuration.toString());
      this.locTimeRestricted.val(local.locTimeRestricted.toString());
      this.locOpenDays.val(local.locOpenDays.toString());
      this.locNumPersonsApo.val(local.locNumPersonsApo.toString());
      this.locNumUsuDays.val(local.locNumUsuDays.toString());
      this.locNewClientDefault[0].options.selectedIndex = local.locNewClientDefault;
      this.locMailBookign[0].options.selectedIndex = local.locMailBookign;
      this.locMulServices[0].options.selectedIndex = local.locMulServices;
      this.locSelCalendar[0].options.selectedIndex = local.locSelCalendar;
      firm = __FacadeCore.Cache_get(appName + "firm");
      if (firm.firConfig.configLocal.configLocNumPer === 0) {
        this.locNumPersonsApo.parent().hide();
      }
      if (firm.firConfig.configLocal.configLocMulSer === 0) {
        this.locMulServices.parent().parent().hide();
      }
      if (firm.firConfig.configLocal.configLocSelCal === 0) {
        return this.locSelCalendar.parent().parent().hide();
      }
    };

    LocalGeneralCtrl.prototype.onSave = function(event) {
      var _this, data, local, url;
      if (this.artLocalGeneral.hasClass("active")) {
        if (this.validateForm()) {
          __FacadeCore.Cache_remove(appName + "elementSave");
          __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
          Lungo.Element.loading(this.buttonSave.selector, "black");
          url = "http://" + appHost + "/local/manager/update";
          local = __FacadeCore.Cache_get(appName + "local");
          data = {
            localId: local.id,
            locBookingClient: this.locBookingClient.val(),
            locName: this.locName.val(),
            locAddress: this.locAddress.val(),
            locCity: this.locCity.val(),
            locState: this.locState.val(),
            locApoDuration: this.locApoDuration.val(),
            locCP: this.locCP.val(),
            locTimeRestricted: this.locTimeRestricted.val(),
            locOpenDays: this.locOpenDays.val(),
            locNumPersonsApo: this.locNumPersonsApo.val(),
            locNumUsuDays: this.locNumUsuDays.val(),
            locNewClientDefault: this.locNewClientDefault.val(),
            locResponName: this.locResponName.val(),
            locResponSurname: this.locResponSurname.val(),
            locResponEmail: this.locResponEmail.val(),
            locResponTelf1: this.locResponTelf1.val(),
            locMailBookign: this.locMailBookign.val(),
            locMulServices: this.locMulServices.val(),
            locSelCalendar: this.locSelCalendar.val()
          };
          _this = this;
          return $$.put(url, data, function(response) {
            __FacadeCore.Cache_remove(appName + "local");
            __FacadeCore.Cache_set(appName + "local", response);
            _this.buttonSave.html(__FacadeCore.Cache_get(appName + "elementSave"));
            return Lungo.Notification.success(findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3);
          });
        }
      }
    };

    LocalGeneralCtrl.prototype.validateForm = function() {
      var result;
      result = true;
      ({
        validateForm: function() {}
      });
      result = true;
      this.error_locName.html("");
      this.error_locAddress.html("");
      this.error_locCity.html("");
      this.error_locState.html("");
      this.error_locCP.html("");
      this.error_locResponName.html("");
      this.error_locResponSurname.html("");
      this.error_locResponEmail.html("");
      this.error_locResponTelf1.html("");
      this.error_locApoDuration.html("");
      this.error_locTimeRestricted.html("");
      this.error_locOpenDays.html("");
      this.error_locNumPersonsApo.html("");
      this.error_locNumUsuDays.html("");
      if (!this.locName[0].checkValidity()) {
        this.error_locName.html(getMessageValidity(this.locName[0]));
        this.locName[0].focus();
        result = false;
      } else if (!this.locAddress[0].checkValidity()) {
        this.error_locAddress.html(getMessageValidity(this.locAddress[0]));
        this.locAddress[0].focus();
        result = false;
      } else if (!this.locCity[0].checkValidity()) {
        this.error_locCity.html(getMessageValidity(this.locCity[0]));
        this.locCity[0].focus();
        result = false;
      } else if (!this.locState[0].checkValidity()) {
        this.error_locState.html(getMessageValidity(this.locState[0]));
        this.locState[0].focus();
        result = false;
      } else if (!this.locCP[0].checkValidity()) {
        this.error_locCP.html(getMessageValidity(this.locCP[0]));
        this.locCP[0].focus();
        result = false;
      } else if (!this.locResponName[0].checkValidity()) {
        this.error_locResponName.html(getMessageValidity(this.locResponName[0]));
        this.locResponName[0].focus();
        result = false;
      } else if (!this.locResponSurname[0].checkValidity()) {
        this.error_locResponSurname.html(getMessageValidity(this.locResponSurname[0]));
        this.locResponSurname[0].focus();
        result = false;
      } else if (!this.locResponEmail[0].checkValidity()) {
        this.error_locResponEmail.html(getMessageValidity(this.locResponEmail[0]));
        this.locResponEmail[0].focus();
        result = false;
      } else if (!this.locResponTelf1[0].checkValidity()) {
        this.error_locResponTelf1.html(getMessageValidity(this.locResponTelf1[0]));
        this.locResponTelf1[0].focus();
        result = false;
      } else if (!checkValidity(this.locApoDuration.val(), this.locApoDuration.attr("pattern"), this.locApoDuration.attr("required"))) {
        this.error_locApoDuration.html(getMessageValidity(this.locApoDuration[0]));
        this.locApoDuration[0].focus();
        result = false;
      } else if (!checkValidity(this.locTimeRestricted.val(), this.locTimeRestricted.attr("pattern"), this.locTimeRestricted.attr("required"))) {
        this.error_locTimeRestricted.html(getMessageValidity(this.locTimeRestricted[0]));
        this.locTimeRestricted[0].focus();
        result = false;
      } else if (!checkValidity(this.locOpenDays.val(), this.locOpenDays.attr("pattern"), this.locOpenDays.attr("required"))) {
        this.error_locOpenDays.html(getMessageValidity(this.locOpenDays[0]));
        this.locOpenDays[0].focus();
        result = false;
      } else if (!checkValidity(this.locNumPersonsApo.val(), this.locNumPersonsApo.attr("pattern"), this.locNumPersonsApo.attr("required"))) {
        this.error_locNumPersonsApo.html(getMessageValidity(this.locNumPersonsApo[0]));
        this.locNumPersonsApo[0].focus();
        result = false;
      } else if (!checkValidity(this.locNumUsuDays.val(), this.locNumUsuDays.attr("pattern"), this.locNumUsuDays.attr("required"))) {
        this.error_locNumUsuDays.html(getMessageValidity(this.locNumUsuDays[0]));
        this.locNumUsuDays[0].focus();
        result = false;
      }
      return result;
    };

    return LocalGeneralCtrl;

  })(Monocle.Controller);

  __Controller.LocalGeneral = new LocalGeneralCtrl("section#booking");

}).call(this);