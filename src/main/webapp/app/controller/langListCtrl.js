// Generated by CoffeeScript 1.12.7
(function() {
  var LangListCtrl,
    extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  LangListCtrl = (function(superClass) {
    extend(LangListCtrl, superClass);

    function LangListCtrl() {
      return LangListCtrl.__super__.constructor.apply(this, arguments);
    }

    LangListCtrl.prototype.events = {
      "load article#langs-admin": "loadLangList",
      "singleTap article#langs-admin li": "onLangEnabled",
      "singleTap a[data-action=save]": "onSave",
      "singleTap a[data-action=reset]": "onReset"
    };

    LangListCtrl.prototype.elements = {
      "#langs-admin": "artListLang",
      "header a[href=\\#]": "header",
      "footer a": "footer",
      "a[data-action=reset]": "buttonReset",
      "a[data-action=save]": "buttonSave"
    };

    LangListCtrl.prototype.onSave = function(event) {
      var _this, data, h, local, selectedLangs, url;
      if (this.artListLang.hasClass("active")) {
        __FacadeCore.Cache_remove(appName + "elementSave");
        __FacadeCore.Cache_set(appName + "elementSave", this.buttonSave.html());
        Lungo.Element.loading(this.buttonSave.selector, "black");
        selectedLangs = new Array();
        h = -1;
        $$("article#langs-admin *[data-lang]").each(function() {
          var lang, langEnabledSelect;
          lang = $$(this).attr("data-lang");
          langEnabledSelect = Lungo.dom("article#langs-admin #langEnabled_" + lang);
          if (langEnabledSelect.hasClass("accept")) {
            h++;
            return selectedLangs[h] = lang;
          }
        });
        url = "http://" + appHost + "/local/manager/changeLangs";
        local = __FacadeCore.Cache_get(appName + "local");
        data = {
          localId: local.id,
          selectedLangs: selectedLangs
        };
        _this = this;
        return $$.put(url, data, function(response) {
          __FacadeCore.Cache_remove(appName + "local");
          __FacadeCore.Cache_set(appName + "local", response);
          _this.buttonSave.html(__FacadeCore.Cache_get(appName + "elementSave"));
          return Lungo.Notification.success(findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3);
        });
      }
    };

    LangListCtrl.prototype.loadLangList = function(event) {
      var _this, url;
      this.header.hide();
      this.footer.hide();
      this.buttonReset.show();
      this.buttonSave.show();
      Lungo.Element.loading("#langs-admin ul", "black");
      url = "http://" + appHost + "/lang/list";
      _this = this;
      return $$.json(url, null, function(response) {
        return _this.showLoadLangList(response);
      });
    };

    LangListCtrl.prototype.showLoadLangList = function(response) {
      var i, lang, langAux, len, local, localLangs, result, texts, view;
      result = Lungo.Core.toArray(response);
      this.artListLang.children().empty();
      texts = {
        cabText: findLangTextElement("label.aside.langsAdmin")
      };
      view = new __View.ListCabView({
        model: texts,
        container: "section#booking article#langs-admin ul"
      });
      view.append(texts);
      for (i = 0, len = result.length; i < len; i++) {
        langAux = result[i];
        lang = new __Model.Lang({
          lanId: langAux.lanId,
          lanCode: langAux.lanCode,
          lanName: langAux.lanName
        });
        view = new __View.LangListView({
          model: lang
        });
        view.append(lang);
      }
      local = __FacadeCore.Cache_get(appName + "local");
      localLangs = local.locLangs;
      return $$("article#langs-admin *[data-lang]").each(function() {
        var langEnabledSelect;
        lang = $$(this).attr("data-lang");
        $$(this).removeClass();
        $$(this).addClass("selectable");
        if (lang && lang === langApp) {
          $$(this).addClass("current active");
        }
        langEnabledSelect = Lungo.dom("article#langs-admin #langEnabled_" + lang);
        langEnabledSelect.removeClass();
        langEnabledSelect.addClass("right tag");
        if (localLangs && lang && Lungo.Core.findByProperty(localLangs, "lanCode", lang)) {
          langEnabledSelect.addClass("accept");
          return langEnabledSelect.html(findLangTextElement("local.langEnabled"));
        } else {
          langEnabledSelect.addClass("cancel");
          return langEnabledSelect.html(findLangTextElement("local.langDisabled"));
        }
      });
    };

    LangListCtrl.prototype.selectLang = function(langSel) {
      var langEnabledSelect, local, localLangs;
      if (langSel !== langApp) {
        local = __FacadeCore.Cache_get(appName + "local");
        localLangs = local.locLangs;
        langEnabledSelect = Lungo.dom("article#langs-admin #langEnabled_" + langSel);
        if (langEnabledSelect.hasClass("accept")) {
          langEnabledSelect.removeClass("accept");
          langEnabledSelect.addClass("cancel");
          return langEnabledSelect.html(findLangTextElement("local.langDisabled"));
        } else {
          langEnabledSelect.removeClass("cancel");
          langEnabledSelect.addClass("accept");
          return langEnabledSelect.html(findLangTextElement("local.langEnabled"));
        }
      }
    };

    LangListCtrl.prototype.onLangEnabled = function(event) {
      var lang;
      lang = $$(event.target).attr("data-lang");
      return this.selectLang(lang);
    };

    LangListCtrl.prototype.onReset = function(event) {
      if (this.artListLang.hasClass("active")) {
        return this.loadLangList(event);
      }
    };

    return LangListCtrl;

  })(Monocle.Controller);

  __Controller.LangList = new LangListCtrl("section#booking");

}).call(this);