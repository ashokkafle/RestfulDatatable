var authenticationErrorCode = "R0004";
var UTIL = {
    _alertIndex: 0,
    init: function () {
    },
    limitText: function (text, limit) {
        var result = text;
        if (result.length > limit) {
            result = result.substring(0, limit) + "...";
        }
        return result;
    },
    loadXMLString: function (txt) {
        if (window.DOMParser) {
            parser = new DOMParser();
            xmlDoc = parser.parseFromString(txt, "text/xml");
        } else { // Internet Explorer
            try {
                xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                xmlDoc.async = "false";
                xmlDoc.loadXML(txt);
            }
            catch (e) {
                UTIL.log("Problem with loadXML - probably IE8");
                if (typeof (ActiveXObject) != "undefined") {
                    var progIDs = [
                        "Msxml2.DOMDocument.6.0",
                        "Msxml2.DOMDocument.5.0",
                        "Msxml2.DOMDocument.4.0",
                        "Msxml2.DOMDocument.3.0",
                        "MSXML2.DOMDocument",
                        "MSXML.DOMDocument"
                    ];
                    for (var i = 0; i < progIDs.length; i++) {
                        try {
                            xmlDoc = new ActiveXObject(progIDs[i]);
                            if (xmlDoc) {
                                return xmlDoc.loadXML(txt);
                            }
                        } catch (e) {
                        }
                        ;
                    }
                }
            }
        }
        return xmlDoc;
    },
    serializeXML: function (node) {
        if (typeof XMLSerializer != "undefined") {
            return (new XMLSerializer()).serializeToString(node);
        } else if (node.xml) {
            return node.xml;
        } else {
            throw "XML.serialize is not supported or can't serialize " + node;
        }
    },
    isCurrency: function (num) {
        var regex = /^\$?((\d,?)+(\.\d*)?|\.\d+)$/;
        return regex.test(num);
    },
    formatCurrency: function (num) {
        return '$' + commaSeparateDigits(this.parseCurrency(num).toFixed(2));
    },
    parseCurrency: function (num) {
        if (typeof num === 'number')
            return num;
        return this.isCurrency(num) ? parseFloat(num.replace(/[,$]/g, '')) : 0;
    },
    calcTotal: function (price, qty) {
        return this.formatCurrency(this.parseCurrency(price) * qty);
    },
    buildXLSData: function (array) {
        var data = "";
        for (i = 0; i < array.length; i++) {
            data += "<row>";
            for (j = 0; j < array[i].length; j++) {
                data += "<cell>" + encodeURIComponent(array[i][j]) + "</cell>";
            }
            data += "</row>";
        }
        return data;
    },
    xmlAppendArray: function (doc, element, array) {
        $(array).each(function () {
            var row = doc.createElement('row');
            $(this).each(function () {
                $(row).append($(doc.createElement('cell')).text('' + this));
            });
            $(element).append($(row));
        });
    },
    addChild: function (parent, child, text) {
        $(child).text(text);
        parent.append(child);
    },
    showComsatError: function (xml) {
        if ($(xml).find('code').text() == authenticationErrorCode) {
            UTIL.sessionExpired();
        }
        else {
            this.showErrorMessage(null, $(xml).find('error[index="0"] message').text());
        }
    },
    showComsatWarnings: function (xml) {
        $(xml).find('error message').each(function () {
            UTIL.showWarning($(this).text());
        });
    },
    showErrorMessage: function (title, message) {
        this.showError(message, title);
    },
    showWarning: function (message, id) {
        this.showMessage('Warning', id, message, 6000);
        //$('html, body').animate({scrollTop: '0px'}, 300);
    },
    showSuccess: function (message, id) {
        this.showMessage('Success', id, message);
        //$('html, body').animate({scrollTop: '0px'}, 300);
    },
    showError: function (message, id) {
        this.showMessage('Error', id, message);
        //$('html, body').animate({scrollTop: '0px'}, 300);
    },
    showMessage: function (type, id, message, fadeMS) {
        var alertIndex = this._alertIndex++;
        if (!id)
            id = "systemMessage";
        $("#" + id)
                .append(
                        $('<div id="alert'
                                + alertIndex
                                + '" class="alerts '
                                + type.toLowerCase()
                                + ' ui-corner-all"><span class="icon sprite"></span><span id="closeButton'
                                + alertIndex
                                + '" class="close" onclick="UTIL.closeMessage('
                                + alertIndex + ')">x</span><strong>'
                                + type + '</strong> ' + message
                                + '</div>'));
        var fade = fadeMS;
        if (fade == undefined && type == 'Success')
            fade = 3000;
        if (fade) {
            setTimeout(function () {
                UTIL.closeMessage(alertIndex);
            }, fade);
        }
        // temp alert if page is missing div with id of systemMessage
        if ($("#" + id).length == 0) {
            alert(message);
        }
    },
    durationHours: function (mills) {
        if (isNaN(mills))
            return '';
        var ms = mills < 0 ? -mills : mills;
        var seconds = parseInt((ms / 1000) % 60);
        var minutes = parseInt((ms / (60000)) % 60);
        var hours = parseInt((ms / (3600000)) % 24);
        return (mills < 0 ? '-' : '') + (hours.toString().length == 1 ? "0" + hours : hours)
                + ":" + (minutes.toString().length == 1 ? "0" + minutes : minutes) + ":"
                + (seconds.toString().length == 1 ? "0" + seconds : seconds);
    },
    getUrlParameter: function (sParam) {
        var sPageURL = window.location.search.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++) {
            var sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] == sParam) {
                return sParameterName[1];
            }
        }
    },
    durationHHMM: function (mills) {
        return UTIL.durationHours(mills).substr(0, 5);
    },
    closeMessage: function (index) {
        $("div#alert" + index).slideUp('slow');
    },
    unescape: function (str) {
        return $("<div/>").html(str).text();
    },
    sessionExpired: function () {
        window.location.href = "/index.jsp?ERROR=SESSION&forward=" + (window.location.href.toString().split(window.location.host)[1]);
        /*
         *	var logoutForm = document.createElement("logoutForm");
         logoutForm.setAttribute("id", "logoutForm");
         logoutForm.setAttribute("method", "post");
         logoutForm.setAttribute("action", "/index.jsp");
         logoutForm.appendChild(document.createElement("input", {type: "hidden", name: "ERROR", value: "SESSION"}));
         logoutForm.appendChild(document.createElement("input", 
         {type: "hidden", name: "forward", value: (window.location.href.toString().split(window.location.host)[1])}));
         document.body.appendChild(logoutForm);
         logoutForm.submit(); //fails... WHY?!?!
         */
    },
    endsWith: function (str, term) {
        return str.lastIndexOf(term) == str.length - term.length;
    },
    disableInteraction: function ($element) {
        $element.attr("disabled", "disabled");
        $element.addClass("ui-state-disabled");
    },
    enableInteraction: function ($element) {
        $element.removeAttr("disabled", "disabled");
        $element.removeClass("ui-state-disabled");
    },
    log: function (msg) {
        if ($('html').is('.ie8, .ie9')) {
            //do nothing
        }
        else {
            console.log(msg);
        }

    },
    hideSiblingsAndToggleSelf: function ($elm, parentType, filter) {
        $elm.closest(parentType).find(filter).each(function () {
            if ($elm.attr("id") != $(this).attr("id")) {
                $(this).hide("fast");
            }
            else {
                if ($elm.css("display") == "none") {
                    $elm.show("fast");
                    $.cookie("openMenuId", $(this).attr("id"), {path: "/"});
                }
                else {
                    $elm.hide("fast");
                    $.cookie("openMenuId", "", {path: "/"});
                }
            }
        });
    },
    showSpinner: function () {
        $.blockUI({
            message: '<img src="images/ajax-loader2.gif" alt="Loading..."/>',
            css: {
                backgroundColor: 'none',
                border: 'none',
                width: 'auto',
                left: '45%'
            },
            overlayCSS: {
                backgroundColor: '#000',
                opacity: 0.30
            }
        });
    }
};
UTIL.init();

function commaSeparateDigits(digi) {
    return digi.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
}
;