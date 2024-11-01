// Generated by CoffeeScript 1.12.7
(function() {
  var ClientNewCtrl,
    extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  ClientNewCtrl = (function(superClass) {
    extend(ClientNewCtrl, superClass);

    function ClientNewCtrl() {
      return ClientNewCtrl.__super__.constructor.apply(this, arguments);
    }

    ClientNewCtrl.prototype.events = {
      "singleTap a[data-action=save]": "onSave",
      "singleTap a[data-action=cancel]": "onCancel",
      "load #newClient": "loadNew",
      "unload #newClient": "unloadNew",
      "change #cliBirthday": "changeBirthday"
    };

    ClientNewCtrl.prototype.elements = {
      "#cliEmail": "cliEmail",
      "#error_cliEmail": "error_cliEmail",
      "#cliGender": "cliGender",
      "#error_cliGender": "error_cliGender",
      "#cliBirthday": "cliBirthday",
      "#error_cliBirthday": "error_cliBirthday",
      "#cliName": "cliName",
      "#error_cliName": "error_cliName",
      "#cliSurname": "cliSurname",
      "#cliTelf1": "cliTelf1",
      "#error_cliTelf1": "error_cliTelf1",
      "#cliTelf2": "cliTelf2",
      "#error_cliTelf2": "error_cliTelf2",
      "#cliDesc": "cliDesc",
      "ul:last-child": "cliInvoices",
      "a[data-action=save]": "buttonSave",
      "a[data-action=cancel]": "buttonCancel"
    };

    ClientNewCtrl.prototype.onSave = function(event) {
      var _this, client, data, url;
      if (this.validateForm()) {
        __FacadeCore.Cache_remove(appName + "elementSave");
        __FacadeCore.Cache_remove(appName + "elementCancel");
        __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
        __FacadeCore.Cache_set(appName + "elementCancel", this.buttonCancel.html());
        Lungo.Element.loading(this.buttonSave.selector, "black");
        Lungo.Element.loading(this.buttonCancel.selector, "black");
        client = __FacadeCore.Cache_get(appName + "client");
        url = "http://" + appHost + "/client/operator/update";
        data = {
          id: client.cliId,
          cliEmail: this.cliEmail.val(),
          cliGender: this.cliGender.val(),
          cliBirthday: this.cliBirthday.val(),
          cliName: this.cliName.val(),
          cliSurname: this.cliSurname.val(),
          cliTelf1: this.cliTelf1.val(),
          cliTelf2: this.cliTelf2.val(),
          cliDesc: this.cliDesc.val()
        };
        _this = this;
        return $$.put(url, data, function() {
          return Lungo.Notification.success(findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, function() {
            __FacadeCore.Router_article("booking", client.router);
            return _this.resetArticle();
          });
        });
      }
    };

    ClientNewCtrl.prototype.validateForm = function() {
      var result;
      result = true;
      this.error_cliEmail.html("");
      this.error_cliGender.html("");
      this.error_cliBirthday.html("");
      this.error_cliName.html("");
      this.error_cliTelf1.html("");
      this.error_cliTelf2.html("");
      if (!this.cliName[0].checkValidity()) {
        this.error_cliName.html(getMessageValidity(this.cliName[0]));
        this.cliName[0].focus();
        result = false;
      } else if (!this.cliEmail[0].checkValidity()) {
        this.error_cliEmail.html(getMessageValidity(this.cliEmail[0]));
        this.cliEmail[0].focus();
        result = false;
      } else if (!checkValidityDate(this.cliBirthday.val(), this.cliBirthday.attr("required"))) {
        this.error_cliBirthday.html(getMessageValidity(this.cliBirthday[0]));
        this.cliBirthday[0].focus();
        result = false;
      } else if (!checkValidity(this.cliTelf1.val(), this.cliTelf1.attr("pattern"), this.cliTelf1.attr("required"))) {
        this.error_cliTelf1.html(getMessageValidity(this.cliTelf1[0]));
        this.cliTelf1[0].focus();
        result = false;
      } else if (!checkValidity(this.cliTelf2.val(), this.cliTelf2.attr("pattern"), this.cliTelf2.attr("required"))) {
        this.error_cliTelf2.html(getMessageValidity(this.cliTelf2[0]));
        this.cliTelf2[0].focus();
        result = false;
      }
      return result;
    };

    ClientNewCtrl.prototype.changeBirthday = function(event) {
      this.error_cliBirthday.html("");
      return this.cliBirthday.val(formatDate(this.cliBirthday.val()));
    };

    ClientNewCtrl.prototype.onCancel = function(event) {
      __FacadeCore.Cache_remove(appName + "elementSave");
      __FacadeCore.Cache_remove(appName + "elementCancel");
      __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
      __FacadeCore.Cache_set(appName + "elementCancel", this.buttonCancel.html());
      Lungo.Element.loading(this.buttonSave.selector, "black");
      Lungo.Element.loading(this.buttonCancel.selector, "black");
      __FacadeCore.Router_back();
      return this.resetArticle();
    };

    ClientNewCtrl.prototype.resetArticle = function() {
      this.buttonSave.html(__FacadeCore.Cache_get(appName + "elementSave"));
      this.buttonCancel.html(__FacadeCore.Cache_get(appName + "elementCancel"));
      this.cliName.val("");
      this.cliEmail.val("");
      this.cliGender[0].options.selectedIndex = 0;
      this.cliBirthday.val("");
      this.cliSurname.val("");
      this.cliTelf1.val("");
      this.cliTelf2.val("");
      this.error_cliEmail.html("");
      this.error_cliGender.html("");
      this.error_cliBirthday.html("");
      this.error_cliName.html("");
      this.error_cliTelf1.html("");
      this.error_cliTelf2.html("");
      this.cliDesc.val("");
      return this.cliInvoices.html("");
    };

    ClientNewCtrl.prototype.unloadNew = function(event) {
      return __FacadeCore.Cache_remove(appName + "client");
    };

    ClientNewCtrl.prototype.loadNew = function(event) {
      var asyn, cliBirthdayDate, cliBirthdayTime, client, data, date, i, len, listEvents, local, strDate, strTasks, url;
      client = __FacadeCore.Cache_get(appName + "client");
      asyn = __FacadeCore.Service_Settings_asyncFalse();
      local = __FacadeCore.Cache_get(appName + "local");
      data = {
        localId: local.id,
        id: client.cliId
      };
      url = "http://" + appHost + "/event/operator/listByClientAgo";
      listEvents = $$.json(url, data);
      __FacadeCore.Service_Settings_async(asyn);
      strTasks = "";
      listEvents = Lungo.Core.orderByProperty(listEvents, "eveStartTime", "desc");
      for (i = 0, len = listEvents.length; i < len; i++) {
        event = listEvents[i];
        date = new Date(event.eveStartTime);
        strDate = dateToStringFormat(date) + ", " + dateToStringHour(date);
        strTasks += "<li>" + strDate + " - " + event.eveLocalTask.lotName + "</li>";
      }
      this.cliEmail.val(client.cliEmail);
      if (client.cliGender === null) {
        client.cliGender = -1;
      }
      this.cliGender[0].options.selectedIndex = client.cliGender + 1;
      cliBirthdayTime = "";
      if (client.cliBirthday) {
        cliBirthdayDate = new Date(client.cliBirthday);
        cliBirthdayTime = dateToStringSim(cliBirthdayDate, "-");
      }
      this.cliBirthday.val(cliBirthdayTime);
      this.cliName.val(client.cliName);
      this.cliSurname.val(client.cliSurname);
      this.cliTelf1.val(client.cliTelf1);
      this.cliTelf2.val(client.cliTelf2);
      this.cliDesc.val(client.cliDesc);
      return this.cliInvoices.append(strTasks);
    };

    return ClientNewCtrl;

  })(Monocle.Controller);

  __Controller.ClientNew = new ClientNewCtrl("section#newClient");

}).call(this);
