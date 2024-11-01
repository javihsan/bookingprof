// Generated by CoffeeScript 1.12.7
(function() {
  var HoursCtrl,
    extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
    hasProp = {}.hasOwnProperty;

  HoursCtrl = (function(superClass) {
    extend(HoursCtrl, superClass);

    function HoursCtrl() {
      return HoursCtrl.__super__.constructor.apply(this, arguments);
    }

    HoursCtrl.prototype.events = {
      "load article#local-hours": "onLoad",
      "singleTap #liHours": "onHours",
      "singleTap #liHoursMon": "onHoursSem",
      "singleTap #liHoursTue": "onHoursSem",
      "singleTap #liHoursWed": "onHoursSem",
      "singleTap #liHoursThu": "onHoursSem",
      "singleTap #liHoursFri": "onHoursSem",
      "singleTap #liHoursSat": "onHoursSem",
      "singleTap #liHoursSun": "onHoursSem",
      "doubleTap #liHoursMon": "onCloseSem",
      "doubleTap #liHoursTue": "onCloseSem",
      "doubleTap #liHoursWed": "onCloseSem",
      "doubleTap #liHoursThu": "onCloseSem",
      "doubleTap #liHoursFri": "onCloseSem",
      "doubleTap #liHoursSat": "onCloseSem",
      "doubleTap #liHoursSun": "onCloseSem"
    };

    HoursCtrl.prototype.elements = {
      "header a[href=\\#]": "header",
      "footer a": "footer",
      "#placeTasks": "placeTasks",
      "#spHours": "spHours",
      "#spHoursMon": "spHoursMon",
      "#spHoursTue": "spHoursTue",
      "#spHoursWed": "spHoursWed",
      "#spHoursThu": "spHoursThu",
      "#spHoursFri": "spHoursFri",
      "#spHoursSat": "spHoursSat",
      "#spHoursSun": "spHoursSun"
    };

    HoursCtrl.prototype.onLoad = function(event) {
      var annual, annuals, asyn, data, date, i, indxHours, len, local, max, selectedDate, strHours, url;
      this.header.hide();
      this.footer.hide();
      local = __FacadeCore.Cache_get(appName + "local");
      if (local) {
        this.spHoursMon.html(getStrDiary(local.locSemanalDiary.semMonDiary));
        this.spHoursTue.html(getStrDiary(local.locSemanalDiary.semTueDiary));
        this.spHoursWed.html(getStrDiary(local.locSemanalDiary.semWedDiary));
        this.spHoursThu.html(getStrDiary(local.locSemanalDiary.semThuDiary));
        this.spHoursFri.html(getStrDiary(local.locSemanalDiary.semFriDiary));
        this.spHoursSat.html(getStrDiary(local.locSemanalDiary.semSatDiary));
        this.spHoursSun.html(getStrDiary(local.locSemanalDiary.semSunDiary));
        asyn = __FacadeCore.Service_Settings_asyncFalse();
        url = "http://" + appHost + "/annual/manager/getAnnualDiaryByDate";
        selectedDate = dateToString(new Date());
        data = {
          localId: local.id,
          selectedDate: selectedDate.toString()
        };
        annuals = Lungo.Core.toArray($$.json(url, data));
        annuals = Lungo.Core.orderByProperty(annuals, "anuDate", "asc");
        __FacadeCore.Service_Settings_async(asyn);
        strHours = "";
        indxHours = -1;
        max = 5;
        for (i = 0, len = annuals.length; i < len; i++) {
          annual = annuals[i];
          date = new Date(annual.anuDate);
          indxHours++;
          if (indxHours <= max) {
            if (indxHours > 0) {
              strHours += " , ";
            }
            if (indxHours === max) {
              strHours += "...";
            } else {
              strHours += dateToStringYearLast(date);
            }
          }
        }
        return this.spHours.html(strHours);
      } else {
        this.spHoursMon.html("");
        this.spHoursTue.html("");
        this.spHoursWed.html("");
        this.spHoursThu.html("");
        this.spHoursFri.html("");
        this.spHoursSat.html("");
        return this.spHoursSun.html("");
      }
    };

    HoursCtrl.prototype.onHoursSem = function(event) {
      var diary, local, sem;
      local = __FacadeCore.Cache_get(appName + "local");
      if (local) {
        sem = event.currentTarget.id.substring(event.currentTarget.id.length - 3);
        diary = eval("local.locSemanalDiary.sem" + sem + "Diary");
        diary.sem = sem;
        __FacadeCore.Cache_remove(appName + "diary");
        __FacadeCore.Cache_set(appName + "diary", diary);
        return __FacadeCore.Router_section("newHours");
      }
    };

    HoursCtrl.prototype.onHours = function(event) {
      var local;
      local = __FacadeCore.Cache_get(appName + "local");
      if (local) {
        return __FacadeCore.Router_section("booking-hours");
      }
    };

    HoursCtrl.prototype.onCloseSem = function(event) {
      var _this, closed, data, diary, local, sem, url;
      if (__FacadeCore.isDoubleTap(event)) {
        local = __FacadeCore.Cache_get(appName + "local");
        if (local) {
          closed = $$("#" + event.currentTarget.id + " span div")[0];
          if (!closed) {
            url = "http://" + appHost + "/diary/manager/update";
            sem = event.currentTarget.id.substring(event.currentTarget.id.length - 3);
            diary = eval("local.locSemanalDiary.sem" + sem + "Diary");
            data = {
              id: diary.id,
              selectedTimes: ""
            };
            _this = this;
            return $$.put(url, data, function() {
              diary.diaTimes = null;
              eval("local.locSemanalDiary.sem" + sem + "Diary = diary");
              eval("_this.spHours" + sem + ".html (getStrDiary(diary))");
              local.locSemanalDiary.closedDiary[local.locSemanalDiary.closedDiary.length] = getSemDayNum(sem);
              return Lungo.Notification.success(findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3);
            });
          }
        }
      }
    };

    return HoursCtrl;

  })(Monocle.Controller);

  __Controller.Hours = new HoursCtrl("section#booking");

}).call(this);
