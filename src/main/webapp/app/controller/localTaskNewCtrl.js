// Generated by CoffeeScript 1.12.7
(function() {
  var LocalTaskNewCtrl,
    extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  LocalTaskNewCtrl = (function(superClass) {
    extend(LocalTaskNewCtrl, superClass);

    function LocalTaskNewCtrl() {
      return LocalTaskNewCtrl.__super__.constructor.apply(this, arguments);
    }

    LocalTaskNewCtrl.prototype.events = {
      "load #localTask-form": "loadNew",
      "singleTap a[data-action=save]": "onSave",
      "singleTap a[data-action=cancel]": "onCancel",
      "singleTap a[data-action=single]": "onSingle",
      "change #lotTaskId": "changeSelectTask",
      "change #lotTaskRate": "changeRate"
    };

    LocalTaskNewCtrl.prototype.elements = {
      "#localTask-form": "localTaskForm",
      "a[data-action=save]": "buttonSave",
      "a[data-action=cancel]": "buttonCancel",
      "a[data-action=combi]": "buttonCombi",
      "a[data-action=single]": "buttonSingle",
      "a[data-action=new]": "buttonNew",
      "#lotTaskId": "selectTask",
      "#localTask-name": "lotNameInputContainer",
      "#lotNameError": "lotNameError",
      "#lotTaskDuration": "lotTaskDuration",
      "#lotTaskDurationError": "lotTaskDurationError",
      "#lotTaskPost": "lotTaskPost",
      "#lotTaskPostError": "lotTaskPostError",
      "#lotTaskRate": "lotTaskRate",
      "#lotTaskRateError": "lotTaskRateError",
      "#symbol_currency": "symbolCurrency",
      "#lotVisible": "lotVisible"
    };

    LocalTaskNewCtrl.prototype.changeRate = function(event) {
      return this.lotTaskRate.val(parseFloat(this.lotTaskRate.val()).toFixed(2));
    };

    LocalTaskNewCtrl.prototype.onSingle = function(event) {
      return __FacadeCore.Router_article("newLocalTask", "localTask-form");
    };

    LocalTaskNewCtrl.prototype.changeSelectTask = function(event) {
      var asyn, data, lotNameInput, nameMulti, nameMultiResult, task, tasks, url;
      tasks = __FacadeCore.Cache_get(appName + "tasks");
      task = Lungo.Core.findByProperty(tasks, "id", this.selectTask.val());
      asyn = __FacadeCore.Service_Settings_asyncFalse();
      url = "http://" + appHost + "/multiText/listByKey";
      data = {
        key: task.tasNameMulti
      };
      nameMultiResult = $$.json(url, data);
      __FacadeCore.Service_Settings_async(asyn);
      nameMulti = Lungo.Core.toArray(nameMultiResult);
      lotNameInput = $$("#localTask-name input[type=text]");
      return lotNameInput.each(function() {
        var nameMultiText;
        nameMultiText = Lungo.Core.findByProperty(nameMulti, "mulLanCode", $$(this).attr("data-lang"));
        return $$(this).val(nameMultiText.mulText);
      });
    };

    LocalTaskNewCtrl.prototype.loadNew = function(event) {
      var asyn, data, firm, i, j, k, l, lang, len, len1, len2, local, localLang, localLangs, localTask, nameMulti, nameMultiResult, nameMultiText, task, taskId, taskOption, tasks, url, view;
      this.buttonCombi.show();
      this.buttonSingle.hide();
      this.buttonNew.hide();
      localTask = __FacadeCore.Cache_get(appName + "localTaskNew");
      local = __FacadeCore.Cache_get(appName + "local");
      this.symbolCurrency.html(local.locWhere.wheCurrency);
      localLangs = Lungo.Core.toArray(local.locLangs);
      localLangs = Lungo.Core.orderByProperty(localLangs, "lanName", "asc");
      lang = Lungo.Core.findByProperty(localLangs, "lanCode", langApp);
      localLangs = changeArrayToFirst(localLangs, lang);
      if (localTask) {
        this.buttonCombi.hide();
        taskId = localTask.lotTaskId;
        asyn = __FacadeCore.Service_Settings_asyncFalse();
        url = "http://" + appHost + "/multiText/listByKey";
        data = {
          key: localTask.lotNameMulti
        };
        nameMultiResult = $$.json(url, data);
        __FacadeCore.Service_Settings_async(asyn);
        nameMulti = Lungo.Core.toArray(nameMultiResult);
        nameMulti = Lungo.Core.orderByProperty(nameMulti, "mulLanName", "asc");
        lang = Lungo.Core.findByProperty(nameMulti, "mulLanCode", langApp);
        nameMulti = changeArrayToFirst(nameMulti, lang);
        this.lotTaskDuration.val(localTask.lotTaskDuration.toString());
        this.lotTaskPost.val(localTask.lotTaskPost.toString());
        this.lotTaskRate.val(parseFloat(localTask.lotTaskRate).toFixed(2));
        if (localTask.lotVisible === 1) {
          this.lotVisible[0].options.selectedIndex = 0;
        } else {
          this.lotVisible[0].options.selectedIndex = 1;
        }
      } else {
        nameMulti = new Array();
        i = -1;
        for (j = 0, len = localLangs.length; j < len; j++) {
          localLang = localLangs[j];
          i++;
          nameMultiText = {
            mulLanName: localLang.lanName,
            mulLanCode: localLang.lanCode
          };
          nameMulti[i] = nameMultiText;
        }
        this.lotTaskDuration.val("");
        this.lotTaskPost.val("");
        this.lotTaskRate.val("");
        this.lotVisible[0].options.selectedIndex = 0;
      }
      firm = __FacadeCore.Cache_get(appName + "firm");
      if (firm.firBilledModule === 0) {
        this.lotTaskRate.parent().hide();
        this.lotTaskRate.val("0");
      }
      this.lotNameInputContainer.empty();
      for (k = 0, len1 = nameMulti.length; k < len1; k++) {
        nameMultiText = nameMulti[k];
        if (!localTask || (localTask && Lungo.Core.findByProperty(localLangs, "lanCode", nameMultiText.mulLanCode))) {
          view = new __View.LocalTaskNameView({
            model: nameMultiText
          });
          view.append(nameMultiText);
        }
      }
      tasks = __FacadeCore.Cache_get(appName + "tasks");
      if (!tasks) {
        asyn = __FacadeCore.Service_Settings_asyncFalse();
        url = "http://" + appHost + "/task/list";
        data = {
          domain: appFirmDomain
        };
        tasks = $$.json(url, data);
        tasks = Lungo.Core.orderByProperty(tasks, "tasName", "asc");
        __FacadeCore.Cache_remove(appName + "tasks");
        __FacadeCore.Cache_set(appName + "tasks", tasks);
        __FacadeCore.Service_Settings_async(asyn);
      }
      i = -1;
      taskOption = 0;
      for (l = 0, len2 = tasks.length; l < len2; l++) {
        task = tasks[l];
        i++;
        this.selectTask[0].options[i] = new Option(task.tasName, task.id);
        if (taskId && taskId === task.id) {
          taskOption = i;
        }
      }
      this.selectTask[0].options.selectedIndex = taskOption;
      if (!localTask) {
        return this.changeSelectTask(event);
      }
    };

    LocalTaskNewCtrl.prototype.onSave = function(event) {
      var _this, data, local, localTask, lotNameInput, url;
      if (this.localTaskForm.hasClass("active")) {
        if (this.validateForm()) {
          __FacadeCore.Cache_remove(appName + "elementSave");
          __FacadeCore.Cache_remove(appName + "elementCancel");
          __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
          __FacadeCore.Cache_set(appName + "elementCancel", this.buttonCancel.html());
          Lungo.Element.loading(this.buttonSave.selector, "black");
          Lungo.Element.loading(this.buttonCancel.selector, "black");
          localTask = __FacadeCore.Cache_get(appName + "localTaskNew");
          local = __FacadeCore.Cache_get(appName + "local");
          data = {
            localId: local.id,
            lotTaskId: this.selectTask.val(),
            lotTaskDuration: this.lotTaskDuration.val(),
            lotTaskPost: this.lotTaskPost.val(),
            lotTaskRate: this.lotTaskRate.val(),
            lotVisible: this.lotVisible.val()
          };
          lotNameInput = $$("#localTask-name input[type=text]");
          lotNameInput.each(function() {
            return eval("data." + $$(this)[0].id + " = '" + $$(this).val() + "'");
          });
          if (localTask) {
            data.id = localTask.lotId;
          }
          url = "http://" + appHost + "/localTask/manager/new";
          _this = this;
          return $$.put(url, data, function(response) {
            if (response.lotDefault) {
              local.locTaskDefaultId = response.id;
            }
            return Lungo.Notification.success(findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, function() {
              __FacadeCore.Router_article("booking", "local-tasks");
              return _this.resetArticle();
            });
          });
        }
      }
    };

    LocalTaskNewCtrl.prototype.validateForm = function() {
      var _this, lotNameInput, result;
      result = true;
      this.lotNameError.html("");
      this.lotTaskDurationError.html("");
      this.lotTaskPostError.html("");
      this.lotTaskRateError.html("");
      lotNameInput = $$("#localTask-name input[type=text]");
      _this = this;
      lotNameInput.each(function() {
        if (result && !$$(this)[0].checkValidity()) {
          _this.lotNameError.html(getMessageValidity($$(this)[0]));
          $$(this)[0].focus();
          return result = false;
        }
      });
      if (result && !checkValidity(this.lotTaskDuration.val(), this.lotTaskDuration.attr("pattern"), this.lotTaskDuration.attr("required"))) {
        this.lotTaskDurationError.html(getMessageValidity(this.lotTaskDuration[0]));
        this.lotTaskDuration[0].focus();
        result = false;
      }
      if (result && !checkValidity(this.lotTaskPost.val(), this.lotTaskPost.attr("pattern"), this.lotTaskPost.attr("required"))) {
        this.lotTaskPostError.html(getMessageValidity(this.lotTaskPost[0]));
        this.lotTaskPost[0].focus();
        result = false;
      }
      if (result && !checkValidity(this.lotTaskRate.val(), this.lotTaskRate.attr("pattern"), this.lotTaskRate.attr("required"))) {
        this.lotTaskRateError.html(getMessageValidity(this.lotTaskRate[0]));
        this.lotTaskRate[0].focus();
        result = false;
      }
      return result;
    };

    LocalTaskNewCtrl.prototype.onCancel = function(event) {
      if (this.localTaskForm.hasClass("active")) {
        __FacadeCore.Cache_remove(appName + "elementSave");
        __FacadeCore.Cache_remove(appName + "elementCancel");
        __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
        __FacadeCore.Cache_set(appName + "elementCancel", this.buttonCancel.html());
        Lungo.Element.loading(this.buttonSave.selector, "black");
        Lungo.Element.loading(this.buttonCancel.selector, "black");
        __FacadeCore.Router_article("booking", "local-tasks");
        return this.resetArticle();
      }
    };

    LocalTaskNewCtrl.prototype.resetArticle = function() {
      this.buttonSave.html(__FacadeCore.Cache_get(appName + "elementSave"));
      this.buttonCancel.html(__FacadeCore.Cache_get(appName + "elementCancel"));
      this.lotNameError.html("");
      this.lotTaskDurationError.html("");
      this.lotTaskPostError.html("");
      this.lotTaskRateError.html("");
      this.lotVisible[0].options.selectedIndex = 0;
      return __FacadeCore.Cache_remove(appName + "localTaskNew");
    };

    return LocalTaskNewCtrl;

  })(Monocle.Controller);

  __Controller.LocalTaskNew = new LocalTaskNewCtrl("section#newLocalTask");

}).call(this);
