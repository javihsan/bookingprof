// Generated by CoffeeScript 1.12.7
(function() {
  var InvoiceNewCtrl,
    extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  InvoiceNewCtrl = (function(superClass) {
    extend(InvoiceNewCtrl, superClass);

    function InvoiceNewCtrl() {
      return InvoiceNewCtrl.__super__.constructor.apply(this, arguments);
    }

    InvoiceNewCtrl.prototype.conserveData = false;

    InvoiceNewCtrl.prototype.events = {
      "load #newInvoice": "loadNew",
      "unload #newInvoice": "unloadNew",
      "singleTap a[data-action=save]": "onSave",
      "singleTap a[data-action=cancel]": "onCancel",
      "singleTap a[data-action=new]": "addTask",
      "singleTap a[data-action=newPro]": "addProduct",
      "singleTap a[data-action=search-client]": "onSearchClient",
      "change #invoice-form input[data-id=bilRate]": "changeRate",
      "change #invDate": "changeDate",
      "change #invClientNew": "changeInvClientNew",
      "change #invClientEmail": "changeCliEmail"
    };

    InvoiceNewCtrl.prototype.elements = {
      "#invoice-form": "invoiceForm",
      "a[data-action=save]": "buttonSave",
      "a[data-action=cancel]": "buttonCancel",
      "a[data-action=new]": "buttonNew",
      "a[data-action=newPro]": "buttonNewProduct"
    };

    InvoiceNewCtrl.prototype.loadNew = function(event) {
      var ICSEvents, _this, asyn, bilTaskIdError, bilTaskIdSelect, billedMod, calendars, data, error_invClientEmail, error_invClientName, error_invClientSurname, eveClient, eventAux, eventFin, i, invClient, invClientEmail, invClientName, invClientNew, invClientSurname, invDate, invDay, invHour, invoiceMod, j, k, len, local, objRate, objSelect, products, ratValueNew, selectedTasksBil, selectedTasksBilNum, simpleLocalTasks, tasks, textsTemplates, url, view;
      if (this.invoiceForm.hasClass("active")) {
        this.buttonSave.show();
        if (this.conserveData) {
          eveClient = __FacadeCore.Cache_get(appName + "selectClient");
          if (eveClient) {
            invClientName = $$("#invClientName");
            invClientSurname = $$("#invClientSurname");
            invClientEmail = $$("#invClientEmail");
            invClientName.val(eveClient.cliName);
            invClientSurname.val(eveClient.cliSurname);
            invClientEmail.val(eveClient.cliEmail);
            error_invClientName = $$("#error_invClientName");
            error_invClientSurname = $$("#error_invClientSurname");
            error_invClientEmail = $$("#error_invClientEmail");
            error_invClientName.html("");
            error_invClientSurname.html("");
            error_invClientEmail.html("");
          }
          selectedTasksBilNum = __FacadeCore.Cache_get(appName + "selectedTasksBilNum");
          if (selectedTasksBilNum) {
            objSelect = Lungo.dom("#invoice-form #bilTaskId" + selectedTasksBilNum);
            selectedTasksBil = __FacadeCore.Cache_get(appName + "bilTaskId" + selectedTasksBilNum);
            if (selectedTasksBil.proName) {
              objSelect.html(selectedTasksBil.proName);
              ratValueNew = selectedTasksBil.proRate;
            } else {
              objSelect.html(selectedTasksBil.lotName);
              ratValueNew = selectedTasksBil.lotTaskRate;
            }
            objRate = Lungo.dom("#invoice-form #bilRate" + selectedTasksBilNum);
            objRate.val(ratValueNew.toString());
            bilTaskIdError = $$("#error_bilTaskId" + selectedTasksBilNum);
            if (bilTaskIdError) {
              bilTaskIdError.hide();
            }
          }
          this.changeRate();
          __FacadeCore.Cache_remove(appName + "selectedTasksBilNum");
        } else {
          eventFin = __FacadeCore.Cache_get(appName + "eventFin");
          simpleLocalTasks = __FacadeCore.Cache_get(appName + "simpleLocalTasks");
          if (!simpleLocalTasks) {
            asyn = __FacadeCore.Service_Settings_asyncFalse();
            url = "http://" + appHost + "/localTask/manager/listOnlySimpleInv";
            local = __FacadeCore.Cache_get(appName + "local");
            data = {
              localId: local.id
            };
            simpleLocalTasks = $$.json(url, data);
            simpleLocalTasks = Lungo.Core.orderByProperty(simpleLocalTasks, "lotName", "asc");
            __FacadeCore.Cache_remove(appName + "simpleLocalTasks");
            __FacadeCore.Cache_set(appName + "simpleLocalTasks", simpleLocalTasks);
            url = "http://" + appHost + "/task/list";
            data = {
              domain: appFirmDomain
            };
            tasks = $$.json(url, data);
            tasks = Lungo.Core.orderByProperty(tasks, "tasName", "asc");
            __FacadeCore.Cache_remove(appName + "tasks");
            __FacadeCore.Cache_set(appName + "tasks", tasks);
            url = "http://" + appHost + "/product/operator/list";
            data = {
              localId: local.id
            };
            products = $$.json(url, data);
            products = Lungo.Core.orderByProperty(products, "proName", "asc");
            __FacadeCore.Cache_remove(appName + "products");
            __FacadeCore.Cache_set(appName + "products", products);
            url = "http://" + appHost + "/calendar/operator/listDiary";
            data = {
              localId: local.id
            };
            calendars = $$.json(url, data);
            calendars = Lungo.Core.orderByProperty(calendars, "calName", "asc");
            __FacadeCore.Cache_remove(appName + "calendars");
            __FacadeCore.Cache_set(appName + "calendars", calendars);
            __FacadeCore.Service_Settings_async(asyn);
          }
          this.invoiceForm.children().empty();
          if (eventFin) {
            asyn = __FacadeCore.Service_Settings_asyncFalse();
            url = "http://" + appHost + "/event/operator/listByICS";
            data = {
              ICS: eventFin.eveICS
            };
            ICSEvents = $$.json(url, data);
            ICSEvents = Lungo.Core.orderByProperty(ICSEvents, "eveStartTime", "asc");
            __FacadeCore.Cache_remove(appName + "ICSEvents");
            __FacadeCore.Cache_set(appName + "ICSEvents", ICSEvents);
            __FacadeCore.Service_Settings_async(asyn);
            invDate = new Date(eventFin.eveStartTime);
            invDay = dateToStringFormat(invDate);
            invHour = dateToStringHour(invDate);
            invClient = eventFin.eveClient.whoName;
            if (eventFin.eveClient.whoSurname) {
              invClient += " " + eventFin.eveClient.whoSurname;
            }
            if (eventFin.eveClient.whoEmail) {
              invClient += " - " + eventFin.eveClient.whoEmail;
            }
            local = __FacadeCore.Cache_get(appName + "local");
            textsTemplates = {
              currency: local.locWhere.wheCurrency,
              rate: findLangTextElement("invoice.rate"),
              date: findLangTextElement("invoice.date"),
              client: findLangTextElement("invoice.client"),
              desc: findLangTextElement("invoice.desc")
            };
            invoiceMod = new __Model.Invoice({
              invId: j,
              invTime: invDay + ' ' + invHour,
              invClient: invClient,
              texts: textsTemplates
            });
            view = new __View.InvoiceView({
              model: invoiceMod
            });
            view.append(invoiceMod);
            textsTemplates = {
              changePush: findLangTextElement("label.html.apoFor3"),
              notBilled: findLangTextElement("localTask.notBilled"),
              currency: local.locWhere.wheCurrency,
              selectTask: findLangTextElement("billed.selectTask"),
              selectCalendar: findLangTextElement("billed.selectCalendar"),
              rate: findLangTextElement("billed.rate"),
              "delete": findLangTextElement("form.delete")
            };
            j = 1;
            for (k = 0, len = ICSEvents.length; k < len; k++) {
              eventAux = ICSEvents[k];
              billedMod = new __Model.Billed({
                bilId: j,
                bilRate: eventAux.eveLocalTask.lotTaskRate,
                texts: textsTemplates
              });
              view = new __View.BilledView({
                model: billedMod
              });
              view.append(billedMod);
              j++;
            }
            bilTaskIdSelect = $$("#invoice-form span[data-id=bilTaskId]");
            _this = this;
            i = 0;
            bilTaskIdSelect.each(function() {
              _this.fillTask($$(this), i, true);
              return i++;
            });
          } else {
            local = __FacadeCore.Cache_get(appName + "local");
            textsTemplates = {
              currency: local.locWhere.wheCurrency,
              rate: findLangTextElement("invoice.rate"),
              date: findLangTextElement("invoice.date"),
              "new": findLangTextElement("invoice.newClient"),
              yes: findLangTextElement("general.yes"),
              no: findLangTextElement("general.no"),
              search: findLangTextElement("form.search"),
              name: findLangTextElement("client.name"),
              surname: findLangTextElement("client.surname"),
              email: findLangTextElement("client.email"),
              desc: findLangTextElement("invoice.desc")
            };
            invDate = newDateTimezone();
            invDay = dateToStringSim(invDate, "-");
            invHour = dateToStringHour(invDate);
            invoiceMod = new __Model.Invoice({
              invId: j,
              invTime: invDay,
              eveHour: invHour,
              texts: textsTemplates
            });
            view = new __View.InvoiceNewView({
              model: invoiceMod
            });
            view.append(invoiceMod);
            if (local.locNewClientDefault === 0) {
              invClientNew = $$("#invClientNew");
              invClientNew.val("0");
              this.changeInvClientNew(event);
            }
          }
        }
        this.conserveData = false;
        this.buttonNew.show();
        products = __FacadeCore.Cache_get(appName + "products");
        if (products && products.length > 0) {
          return this.buttonNewProduct.show();
        } else {
          return this.buttonNewProduct.hide();
        }
      }
    };

    InvoiceNewCtrl.prototype.fillTask = function(objSel, ind, sel) {
      var ICSEvents, bilTaskIdError, calendar, calendarId, calendars, eventFin, i, k, len, localTaskSel, objCalendarSel, objRate, ratValueNew, selectedTasksBil, taskOption;
      objCalendarSel = Lungo.dom("#invoice-form #bilCalendarId" + (ind + 1));
      if (sel) {
        eventFin = __FacadeCore.Cache_get(appName + "eventFin");
        if (eventFin) {
          ICSEvents = __FacadeCore.Cache_get(appName + "ICSEvents");
          localTaskSel = ICSEvents[ind].eveLocalTask;
          if (localTaskSel.lotTaskRate === 0) {
            bilTaskIdError = $$("#error_bilTaskId" + (ind + 1));
            if (bilTaskIdError) {
              bilTaskIdError.show();
            }
          }
          __FacadeCore.Cache_remove(appName + "bilTaskId" + (ind + 1));
          __FacadeCore.Cache_set(appName + "bilTaskId" + (ind + 1), localTaskSel);
          calendarId = ICSEvents[ind].eveCalendarId;
        }
      }
      selectedTasksBil = __FacadeCore.Cache_get(appName + "bilTaskId" + (ind + 1));
      if (selectedTasksBil.proName) {
        objSel.html(selectedTasksBil.proName);
        ratValueNew = selectedTasksBil.proRate;
      } else {
        objSel.html(selectedTasksBil.lotName);
        ratValueNew = selectedTasksBil.lotTaskRate;
      }
      objRate = Lungo.dom("#invoice-form #bilRate" + (ind + 1));
      objRate.val(ratValueNew.toString());
      calendars = __FacadeCore.Cache_get(appName + "calendars");
      i = -1;
      taskOption = 0;
      for (k = 0, len = calendars.length; k < len; k++) {
        calendar = calendars[k];
        i++;
        objCalendarSel[0].options[i] = new Option(calendar.calName, calendar.id);
        if (sel) {
          if (calendarId === calendar.id) {
            taskOption = i;
          }
        }
      }
      objCalendarSel[0].options.selectedIndex = taskOption;
      return this.changeRate();
    };

    InvoiceNewCtrl.prototype.addTask = function(event) {
      var billedMod, firstTask, i, local, objSelect, simpleLocalTasks, textsTemplates, view;
      i = 1;
      while ((Lungo.dom("#invoice-form #bilTaskId" + i))[0]) {
        i++;
      }
      simpleLocalTasks = __FacadeCore.Cache_get(appName + "simpleLocalTasks");
      firstTask = simpleLocalTasks[0];
      local = __FacadeCore.Cache_get(appName + "local");
      textsTemplates = {
        changePush: findLangTextElement("label.html.apoFor3"),
        notBilled: findLangTextElement("localTask.notBilled"),
        currency: local.locWhere.wheCurrency,
        selectTask: findLangTextElement("billed.selectTask"),
        selectCalendar: findLangTextElement("billed.selectCalendar"),
        rate: findLangTextElement("billed.rate"),
        "delete": findLangTextElement("form.delete")
      };
      billedMod = new __Model.Billed({
        bilId: i,
        bilRate: firstTask.lotTaskRate,
        texts: textsTemplates
      });
      view = new __View.BilledView({
        model: billedMod
      });
      view.append(billedMod);
      __FacadeCore.Cache_remove(appName + "bilTaskId" + i);
      __FacadeCore.Cache_set(appName + "bilTaskId" + i, firstTask);
      objSelect = Lungo.dom("#invoice-form #bilTaskId" + i);
      return this.fillTask(objSelect, i - 1, false);
    };

    InvoiceNewCtrl.prototype.addProduct = function(event) {
      var billedMod, firstProduct, i, local, objSelect, products, textsTemplates, view;
      i = 1;
      while ((Lungo.dom("#invoice-form #bilTaskId" + i))[0]) {
        i++;
      }
      products = __FacadeCore.Cache_get(appName + "products");
      firstProduct = products[0];
      local = __FacadeCore.Cache_get(appName + "local");
      textsTemplates = {
        changePush: findLangTextElement("label.html.apoFor3"),
        notBilled: findLangTextElement("localTask.notBilled"),
        currency: local.locWhere.wheCurrency,
        selectTask: findLangTextElement("billed.selectProduct"),
        selectCalendar: findLangTextElement("billed.selectCalendar"),
        rate: findLangTextElement("billed.rate"),
        "delete": findLangTextElement("form.delete")
      };
      billedMod = new __Model.Billed({
        bilId: i,
        bilRate: firstProduct.proRate,
        texts: textsTemplates
      });
      view = new __View.BilledView({
        model: billedMod
      });
      view.append(billedMod);
      __FacadeCore.Cache_remove(appName + "bilTaskId" + i);
      __FacadeCore.Cache_set(appName + "bilTaskId" + i, firstProduct);
      objSelect = Lungo.dom("#invoice-form #bilTaskId" + i);
      objSelect.attr("data-product", "1");
      return this.fillTask(objSelect, i - 1, false);
    };

    InvoiceNewCtrl.prototype.changeRate = function(event) {
      var bilRateId, objRateTotal, rate;
      rate = parseFloat(0);
      bilRateId = $$("#invoice-form input[data-id=bilRate]");
      bilRateId.each(function() {
        var valueFloat;
        valueFloat = parseFloat($$(this).val());
        $$(this).val(valueFloat.toFixed(2));
        return rate += valueFloat;
      });
      objRateTotal = Lungo.dom("#invoice-form #invRate");
      return objRateTotal.html(parseFloat(rate).toFixed(2));
    };

    InvoiceNewCtrl.prototype.changeDate = function(event) {
      var error_invDate, invDate;
      invDate = $$("#invDate");
      error_invDate = $$("#error_invDate");
      error_invDate.html("");
      return invDate.val(formatDate(invDate.val()));
    };

    InvoiceNewCtrl.prototype.onSearchClient = function(event) {
      this.conserveData = true;
      __FacadeCore.Cache_remove(appName + "routerSearchClient");
      __FacadeCore.Cache_set(appName + "routerSearchClient", "newInvoice");
      return __FacadeCore.Router_article("searchClient", "search-clients");
    };

    InvoiceNewCtrl.prototype.changeInvClientNew = function(event) {
      var buttonSearch, error_invClientEmail, error_invClientName, error_invClientSurname, invClientEmail, invClientName, invClientNew, invClientSurname;
      invClientNew = $$("#invClientNew");
      buttonSearch = $$("#invoice-form #searchLi");
      invClientName = $$("#invClientName");
      error_invClientName = $$("#error_invClientName");
      invClientSurname = $$("#invClientSurname");
      error_invClientSurname = $$("#error_invClientSurname");
      invClientEmail = $$("#invClientEmail");
      error_invClientEmail = $$("#error_invClientEmail");
      if (invClientNew.val() === "1") {
        invClientName[0].disabled = false;
        invClientSurname[0].disabled = false;
        invClientEmail[0].disabled = false;
        buttonSearch.hide();
      } else {
        invClientName[0].disabled = true;
        invClientSurname[0].disabled = true;
        invClientEmail[0].disabled = true;
        buttonSearch.show();
      }
      error_invClientName.html("");
      error_invClientSurname.html("");
      return error_invClientEmail.html("");
    };

    InvoiceNewCtrl.prototype.changeCliEmail = function(event) {
      var asyn, data, invClient, invClientEmail, invClientName, invClientNew, invClientSurname, invTime, url;
      invTime = $$("#invTime");
      invClientEmail = $$("#invClientEmail");
      if (invTime[0] && invClientEmail.val().length > 0) {
        asyn = __FacadeCore.Service_Settings_asyncFalse();
        url = "http://" + appHost + "/client/operator/listByEmail";
        data = {
          domain: appFirmDomain,
          email: invClientEmail.val()
        };
        invClient = $$.json(url, data);
        __FacadeCore.Service_Settings_async(asyn);
        invClientNew = $$("#invClientNew");
        invClientName = $$("#invClientName");
        invClientSurname = $$("#invClientSurname");
        if (invClient && invClientNew.val() === "1") {
          invClientNew[0].options[1].selected = true;
          Lungo.Notification.success(findLangTextElement("label.notification.existsClient.title"), findLangTextElement("label.notification.existsClient.text"), null, 3);
          invClient.cliId = invClient.id;
          invClientName.val(invClient.whoName);
          invClientSurname.val(invClient.whoSurname);
          this.changeInvClientNew(event);
          __FacadeCore.Cache_remove(appName + "selectClient");
          __FacadeCore.Cache_set(appName + "selectClient", invClient);
          return false;
        }
      }
      return true;
    };

    InvoiceNewCtrl.prototype.onSave = function(event) {
      var _this, a, bilCalendarId, bilRateId, bilTaskId, bilTaskIdError, billedTaskError, data, eventFin, i, invClient, invClientEmail, invClientName, invClientNew, invClientSurname, invDate, invDesc, invTime, invTimeData, local, strBilCalendarId, strBilRateId, strBilTaskId, strBilTypeId, url;
      if (this.validateForm() && this.changeCliEmail(event)) {
        __FacadeCore.Cache_remove(appName + "elementSave");
        __FacadeCore.Cache_remove(appName + "elementCancel");
        __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
        __FacadeCore.Cache_set(appName + "elementCancel", this.buttonCancel.html());
        bilTaskId = $$("#invoice-form span[data-id=bilTaskId]");
        __FacadeCore.Cache_remove(appName + "bilTaskId");
        __FacadeCore.Cache_set(appName + "bilTaskId", bilTaskId);
        Lungo.Element.loading(this.buttonSave.selector, "black");
        Lungo.Element.loading(this.buttonCancel.selector, "black");
        if (bilTaskId.length > 0) {
          bilTaskIdError = $$("#invoice-form div[data-id=bilTaskIdError]");
          billedTaskError = false;
          bilTaskIdError.each(function() {
            var style;
            style = ($$(this)).attr("style");
            if (!billedTaskError && parseInt(style.indexOf("block")) >= 0) {
              return billedTaskError = true;
            }
          });
          if (billedTaskError) {
            _this = this;
            return Lungo.Notification.error(findLangTextElement("label.notification.invoiceBillableTask.title"), findLangTextElement("label.notification.invoiceBillableTask.text"), null, 3, function() {
              _this.buttonSave.html(__FacadeCore.Cache_get(appName + "elementSave"));
              return _this.buttonCancel.html(__FacadeCore.Cache_get(appName + "elementCancel"));
            });
          } else {
            strBilTaskId = "";
            strBilTypeId = "";
            i = 0;
            bilTaskId.each(function() {
              var bilTask;
              if (i > 0) {
                strBilTaskId += ",";
                strBilTypeId += ",";
              }
              bilTask = __FacadeCore.Cache_get(appName + $$(this).attr("id"));
              if (bilTask.id) {
                strBilTaskId += bilTask.id;
              } else {
                strBilTaskId += bilTask.lotId;
              }
              if ($$(this).attr("data-product")) {
                strBilTypeId += "1";
              } else {
                strBilTypeId += "0";
              }
              return i++;
            });
            bilCalendarId = $$("#invoice-form select[data-id=bilCalendarId]");
            strBilCalendarId = "";
            i = 0;
            bilCalendarId.each(function() {
              if (i > 0) {
                strBilCalendarId += ",";
              }
              strBilCalendarId += $$(this).val();
              return i++;
            });
            bilRateId = $$("#invoice-form input[data-id=bilRate]");
            strBilRateId = "";
            i = 0;
            bilRateId.each(function() {
              if (i > 0) {
                strBilRateId += ",";
              }
              strBilRateId += parseFloat($$(this).val().replace(',', '.'));
              return i++;
            });
            url = "http://" + appHost + "/invoice/operator/new";
            local = __FacadeCore.Cache_get(appName + "local");
            invDesc = $$("#invDesc");
            data = {
              localId: local.id,
              invDesc: invDesc.val(),
              bilTaskId: strBilTaskId.toString(),
              bilTypeId: strBilTypeId.toString(),
              bilCalendarId: strBilCalendarId.toString(),
              bilRateId: strBilRateId.toString()
            };
            eventFin = __FacadeCore.Cache_get(appName + "eventFin");
            if (eventFin) {
              data.invClientId = eventFin.eveClient.id;
              data.invTime = eventFin.eveStartTime;
              data.ICS = eventFin.eveICS;
            } else {
              invTime = $$("#invTime");
              invDate = $$("#invDate");
              invClientName = $$("#invClientName");
              invClientSurname = $$("#invClientSurname");
              invClientEmail = $$("#invClientEmail");
              a = invTime.val().split(':');
              invTimeData = new Date(invDate.val());
              invTimeData.setUTCHours(a[0]);
              invTimeData.setUTCMinutes(a[1]);
              invTimeData = invTimeData.getTime();
              data.invTime = invTimeData;
              invClientNew = $$("#invClientNew");
              if (invClientNew.val() === "0") {
                invClient = __FacadeCore.Cache_get(appName + "selectClient");
              }
              if (invClient) {
                data.invClientId = invClient.cliId;
              } else {
                data.cliName = invClientName.val();
                data.cliSurname = invClientSurname.val();
                data.cliEmail = invClientEmail.val();
              }
            }
            _this = this;
            return $$.put(url, data, function() {
              return Lungo.Notification.success(findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, function() {
                return __FacadeCore.Router_article("booking", "list-invoices");
              });
            });
          }
        } else {
          _this = this;
          return Lungo.Notification.error(findLangTextElement("label.notification.invoiceOneTask.title"), findLangTextElement("label.notification.invoiceOneTask.text"), null, 3, function() {
            _this.buttonSave.html(__FacadeCore.Cache_get(appName + "elementSave"));
            return _this.buttonCancel.html(__FacadeCore.Cache_get(appName + "elementCancel"));
          });
        }
      }
    };

    InvoiceNewCtrl.prototype.validateForm = function() {
      var bilRate, bilRateError, bilRateId, bilRateIdError, error_invClientEmail, error_invClientName, error_invClientSurname, error_invDate, error_invTime, i, invClientEmail, invClientName, invClientSurname, invDate, invTime, k, ref, result;
      result = true;
      invTime = $$("#invTime");
      if (invTime[0]) {
        error_invTime = $$("#error_invTime");
        invDate = $$("#invDate");
        error_invDate = $$("#error_invDate");
        invClientName = $$("#invClientName");
        error_invClientName = $$("#error_invClientName");
        invClientSurname = $$("#invClientSurname");
        error_invClientSurname = $$("#error_invClientSurname");
        invClientEmail = $$("#invClientEmail");
        error_invClientEmail = $$("#error_invClientEmail");
        error_invTime.html("");
        error_invDate.html("");
        error_invClientName.html("");
        error_invClientSurname.html("");
        error_invClientEmail.html("");
        if (!checkValidityDate(invDate.val(), invDate.attr("required"))) {
          error_invDate.html(getMessageValidity(invDate[0]));
          invDate[0].focus();
          result = false;
        } else if (!invTime[0].checkValidity()) {
          error_invTime.html(getMessageValidity(invTime[0]));
          invTime[0].focus();
          result = false;
        } else if ((invClientName[0].disabled && !checkValidity(invClientName.val(), invClientName.attr("pattern"), invClientName.attr("required"))) || (!invClientName[0].disabled && !invClientName[0].checkValidity())) {
          error_invClientName.html(getMessageValidity(invClientName[0]));
          invClientName[0].focus();
          result = false;
        } else if (!invClientEmail[0].checkValidity()) {
          error_invClientEmail.html(getMessageValidity(invClientEmail[0]));
          invClientEmail[0].focus();
          result = false;
        }
      }
      bilRateIdError = $$("#invoice-form label[data-id=bilRateError]");
      bilRateIdError.each(function() {
        return $$(this).html("");
      });
      bilRateId = $$("#invoice-form input[data-id=bilRate]");
      if (result && bilRateId.length > 0) {
        for (i = k = 0, ref = bilRateId.length - 1; 0 <= ref ? k <= ref : k >= ref; i = 0 <= ref ? ++k : --k) {
          bilRate = bilRateId[i];
          bilRateError = $$(bilRateIdError[i]);
          if (!bilRate.checkValidity()) {
            bilRateError.html(getMessageValidity(bilRate));
            bilRate.focus();
            result = false;
            break;
          }
        }
      }
      return result;
    };

    InvoiceNewCtrl.prototype.onCancel = function(event) {
      var bilTaskId;
      if (this.invoiceForm.hasClass("active")) {
        __FacadeCore.Cache_remove(appName + "elementSave");
        __FacadeCore.Cache_remove(appName + "elementCancel");
        __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
        __FacadeCore.Cache_set(appName + "elementCancel", this.buttonCancel.html());
        bilTaskId = $$("#invoice-form span[data-id=bilTaskId]");
        __FacadeCore.Cache_remove(appName + "bilTaskId");
        __FacadeCore.Cache_set(appName + "bilTaskId", bilTaskId);
        Lungo.Element.loading(this.buttonSave.selector, "black");
        Lungo.Element.loading(this.buttonCancel.selector, "black");
        return __FacadeCore.Router_section("booking");
      }
    };

    InvoiceNewCtrl.prototype.resetArticle = function() {
      this.buttonSave.html(__FacadeCore.Cache_get(appName + "elementSave"));
      this.buttonCancel.html(__FacadeCore.Cache_get(appName + "elementCancel"));
      return this.invoiceForm.children().empty();
    };

    InvoiceNewCtrl.prototype.unloadNew = function(event) {
      var bilTaskId;
      if (this.invoiceForm.hasClass("active")) {
        if (!this.conserveData) {
          this.resetArticle();
          __FacadeCore.Cache_remove(appName + "eventFin");
          __FacadeCore.Cache_remove(appName + "simpleLocalTasks");
          __FacadeCore.Cache_remove(appName + "tasks");
          __FacadeCore.Cache_remove(appName + "products");
          __FacadeCore.Cache_remove(appName + "calendars");
          __FacadeCore.Cache_remove(appName + "ICSEvents");
          bilTaskId = __FacadeCore.Cache_get(appName + "bilTaskId");
          bilTaskId.each(function() {
            return __FacadeCore.Cache_remove(appName + $$(this).attr("id"));
          });
        }
        return __FacadeCore.Cache_remove(appName + "selectClient");
      }
    };

    return InvoiceNewCtrl;

  })(Monocle.Controller);

  __Controller.InvoiceNew = new InvoiceNewCtrl("section#newInvoice");

}).call(this);
