/**
 * Created by Sung9 on 2017. 7. 20..
 */

/**
 * Created by Sung9 on 2017. 7. 20..
 */

var evi_start_offset = [];
var evi_end_offset = [];
var evi_str = [];
var s_stc_id = [];
var feed = [];
var userName = '';
var temp_pro = '';

function scroll() {
    //console.log('scroll');
    var set = $("#wrapper").offset();
    $('html, body').animate({scrollTop: set.top}, 400);
}

$(document).ready(function () {
    $("#login_btn").click(function (event) {
        event.preventDefault();
        var name = $("#name").val();
        var pass = $("#pass").val();

        var input = JSON.stringify({"name": name, "pass": pass});

        $.ajax({
            type: "POST",
            url: "http://143.248.135.216:18503/pl3/check_user",
            data: input,
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                console.log(data);
                Success(data, name);
            },
            failure: function (err_msg) {
                console.log(err_msg);
            }
        });
    });
});

function Success(data, name) {
    if (data.success == 'pass') {
        userName = name;
        document.getElementById("login").style.display = "none";
        document.getElementById("wrapper").style.display = "";
        document.getElementById("body").style.background = "white";
        login_complete();
    } else if (data.success == 'reject') {
        alert("Login failed...");
        window.location.reload();
    } else {
        console.log("System Fail!");
    }
}

//$(document).ready(function () {
function login_complete() {

    $.ajax({
        type: "POST",
        url: "http://143.248.135.216:18503/pl3/list_prop",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({"p": "hi"}),
        success: function (data) {
            console.log(data);
            property_list(data);
        },
        failure: function (err_msg) {
            console.log('ouch!')
            console.log(err_msg);
        },
        beforeSend: function () {
            var width = 0;
            var height = 0;
            var left = 0;
            var top = 0;

            width = 50;
            height = 50;

            top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
            left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

            if ($("#div_ajax_load_image").length != 0) {
                $("#div_ajax_load_image").css({
                    "top": top + "px",
                    "left": left + "px"
                });
                $("#div_ajax_load_image").show();
            }
            else {
                //$('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#ffffff; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="ajax_loader4.gif" style="width:100px; height:100px;"></div>');
            }
        },
        complete: function () {
            $("#div_ajax_load_image").hide();
        }
    });
}

function property_list(property) {
    var prop = [];
    var total = [];
    var done = [];

    for (var i = 0; i < property.length; i++) {
        prop.push(property[i].prop);
        total.push(property[i].total);
        done.push(property[i].done);
    }
    var html_list = '';

    for (var i = 0; i < prop.length; i++) {
        if (total[i] != done[i]) {
            html_list = html_list +
                '<li style="text-align: center"><a id="' + prop[i] + '" onclick="property_click(this.id)">' + prop[i] + ' ( ' + done[i] + ' / ' + total[i] + ' )</a></li>'
        }
    }

    $("#side-menu").html(html_list);
}

function show_property() {
    $.ajax({
        type: "POST",
        url: "http://143.248.135.216:18503/pl3/list_prop",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({"p": "hi"}),
        success: function (data) {
            console.log(data);
            property_list(data);
        },
        failure: function (err_msg) {
            console.log('ouch!')
            console.log(err_msg);
        },
        beforeSend: function () {
            var width = 0;
            var height = 0;
            var left = 0;
            var top = 0;

            width = 50;
            height = 50;

            top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
            left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

            if ($("#div_ajax_load_image").length != 0) {
                $("#div_ajax_load_image").css({
                    "top": top + "px",
                    "left": left + "px"
                });
                $("#div_ajax_load_image").show();
            }
            else {
                //$('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#ffffff; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="ajax_loader4.gif" style="width:100px; height:100px;"></div>');
            }
        },
        complete: function () {
            $("#div_ajax_load_image").hide();
        }
    });
}

function property_click(selected_property) {
    console.log(s_stc_id.length);

    temp_pro = selected_property;
    evi_start_offset = [];
    evi_end_offset = [];
    evi_str = [];
    s_stc_id = [];
    feed = [];

    var property = selected_property;
    console.log(property);

    $.ajax({
        type: "POST",
        url: "http://143.248.135.216:18503/pl3/samp_extr",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({"p": property}),
        success: function (data) {
            console.log(data);
            tbl_show(data);
            scroll();
        },
        failure: function (err_msg) {
            console.log('ouch!')
            console.log(err_msg);
        },
        beforeSend: function () {
            var width = 0;
            var height = 0;
            var left = 0;
            var top = 0;

            width = 50;
            height = 50;

            top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
            left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

            if ($("#div_ajax_load_image").length != 0) {
                $("#div_ajax_load_image").css({
                    "top": top + "px",
                    "left": left + "px"
                });
                $("#div_ajax_load_image").show();
            }
            else {
                //$('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#ffffff; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="ajax_loader4.gif" style="width:100px; height:100px;"></div>');
            }
        },
        complete: function () {
            $("#div_ajax_load_image").hide();
        }
    });

}

function tbl_show(rest_result) {
    var stc = [];
    var triple = [];
    var temp_triple = [];
    var s = [];
    var p = [];
    var o = [];
    var stc_id = [];
    var html_tbl = '';

    var property = rest_result.p;
    for (var i = 0; i < rest_result.samp.length; i++) {
        stc.push(rest_result.samp[i].stc);
        triple.push(rest_result.samp[i].triple);
        s_stc_id.push(rest_result.samp[i].id);

        //console.log(stc[i]);
        //console.log(triple[i]);
        //console.log(id[i]);
    }

    for (var i = 0 ; i < triple.length; i++){
        var temp = triple[i].substring(1, triple[i].length-1);
        temp_triple[i] = temp.split(',');
        s[i] = temp_triple[i][0].trim();
        p[i] = temp_triple[i][1].trim();
        o[i] = temp_triple[i][2].trim();
    }

    for (var i = 0; i < stc.length; i++) {
        html_tbl = html_tbl + '<tr id="tr_' + i + '">' +
            '<td id="triple_' + i + '"><a href="http://ko.dbpedia.org/resource/'+ s[i] +'" onclick="window.open(this.href); return false">' + s[i] + '</a> - <a href="http://dbpedia.org/ontology/' + p[i] +'" onclick="window.open(this.href); return false">'+ p[i] +'</a> - <a href="http://ko.dbpedia.org/resource/'+ o[i] +'" onclick="window.open(this.href); return false">' + o[i] + '</a></td>' +
            '<td id="stc_' + i + '">' + stc[i] + '</td>' +
            '<td id="td_evidence_' + i + '"></td>' +
            '<td align="center">' +
            '<div class="btn-group" data-toggle="buttons">' +
            '<button style="color: green" id="lbl_' + i + '" class="btn btn" onclick="dragText(this.id)">' +
            '<input type="radio" name="option_' + i + '" id="btn_O_' + i + '" autocomplete="off"  value="1"> O ' +
            '</button>' +
            '<button style="color: red" id="lbl_' + i + '" class="btn btn" onclick="clear2(this.id)">' +
            '<input type="radio" name="option_' + i + '" id="btn_X_' + i + '" autocomplete="off" value="-1"> X ' +
            '</button>' +
            '</div>' +
            //'<button class="label label-success label-as-badge" style="margin-right: 10px" id="btn_O_' + i + '" value="1" onclick="btn_switch_O(btn_O_'+ i +', btn_X_'+ i +')">O</button>' +
            //'<button class="label label-danger label-as-badge" style="margin-left: 10px" id="btn_X_' + i + '" value="-1" onclick="btn_switch_X(btn_O_'+ i +', btn_X_'+ i +')">X</button>' +
            '</td>' +
            '</tr>';
    }
    $("#tbl_body").html(html_tbl);
}
function clear2(id) {
    var row = id.replace('lbl_', "");
    $("#td_evidence_" + row).empty();
    evi_str[row] = 'undefined';
    evi_start_offset[row] = -1;
    evi_end_offset[row] = -1;
}

function data_saver() {
    //var feedback = $(':input[name=option_'+ i +']:radio:checked').val();
    //console.log(feed);
    for (var i = 0; i < s_stc_id.length; i++) {
        feed[i] = ($(':input[name=option_' + i + ']:radio:checked').val());
    }
    //console.log(feed);
}


function JSON_maker() {
    //result = new Object();
    //user['userName'] = 'dummy';

    for (var i = 0; i < s_stc_id.length; i++) {
        if (evi_start_offset[i] == null) {
            evi_start_offset[i] = -1;
        }
        if (evi_end_offset[i] == null) {
            evi_end_offset[i] = -1;
        }
    }

    var result = '{ "userName":"' + userName + '",' +
        '"extrArr":[';
    for (var i = 0; i < s_stc_id.length; i++) {
        result = result + '{' +
            '"id":' + s_stc_id[i] + ',' +
            '"feed":' + feed[i] + ',' +
            '"evi_start_offset":' + evi_start_offset[i] + ',' +
            '"evi_end_offset":' + evi_end_offset[i] + ',';

        if (i == s_stc_id.length - 1) {
            result = result + '"evi_str":"' + evi_str[i] + '"}'
        }
        else {
            result = result + '"evi_str":"' + evi_str[i] + '"},'
        }
    }
    result = result + '] }'

    //console.log(result);
    return result;
}

function btn_switch_O(btn_O_id, btn_X_id) {
    console.log(btn_O_id);
    console.log(btn_X_id);
}

function btn_switch_X(btn_O_id, btn_X_id) {
    console.log(btn_X_id);
    console.log(btn_O_id);
}

function selectInfo() {
    return document.getSelection().getRangeAt(0);
}

function dragText(clicked_id) {
    console.log("클릭함 " + clicked_id);
    console.log();
    if (document.getSelection()) {
        var selectionS;
        var selectionE;
        var selectionT;
        var selectedRow;
        selectedRow = clicked_id.replace("lbl_", "");
        selectionS = selectInfo().startOffset.valueOf();
        selectionE = selectInfo().endOffset.valueOf();
        selectionT = selectInfo().toString().valueOf();
        if (selectionE > selectionS) {
            console.log("start/end/text : " + selectionS + "/" + selectionE + "/" + selectionT);
            $("#td_evidence_" + selectedRow).html(selectionT);
            console.log(selectionT);
            evi_str[selectedRow] = selectionT.replace(/\"/g, "\\\"");
            console.log(selectionS);
            evi_start_offset[selectedRow] = selectionS;
            console.log(selectionE);
            evi_end_offset[selectedRow] = selectionE;
            console.log(evi_str);
        }
        else if (selectionS == selectionE) {
            $("#btn_O_" + selectedRow).prop('checked', false);
            $("#btn_X_" + selectedRow).prop('checked', true);
            btn = document.getElementById('btn_O_' + selectedRow);
            btn.disabled = 'disabled';
            console.log("#btn_O_" + selectedRow)
            console.log(selectedRow);
        }
        else {
            console.log("s/e : " + selectionS + "/" + selectionE);
            console.log(selectionT.valueOf());
        }
    }
    else {
        //console.log("안눌렸어 바보야");
        $("#btn_O_" + selectedRow).prop('checked', false);
        $("#btn_X_" + selectedRow).prop('checked', true);
    }
}

$(document).ready(function () {
    $("#btn_submit").click(function (event) {
        data_saver();
        var working_checker = working_check();
        if (working_checker == 0) {
            var result = JSON_maker();
            console.log(result);
            $.ajax({
                type: "POST",
                url: "http://143.248.135.216:18503/pl3/update_feedback",
                contentType: "application/json",
                dataType: "json",
                data: result,
                success: function (data) {
                    show_property();
                    //console.log(data);
                    property_click(temp_pro);
                    //console.log('clear');
                },
                failure: function (err_msg) {
                    console.log('ouch!')
                    console.log(err_msg);
                },
                beforeSend: function () {
                    var width = 0;
                    var height = 0;
                    var left = 0;
                    var top = 0;

                    width = 50;
                    height = 50;

                    top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
                    left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

                    if ($("#div_ajax_load_image").length != 0) {
                        $("#div_ajax_load_image").css({
                            "top": top + "px",
                            "left": left + "px"
                        });
                        $("#div_ajax_load_image").show();
                    }
                    else {
                        //$('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#ffffff; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="ajax_loader4.gif" style="width:100px; height:100px;"></div>');
                    }
                },
                complete: function () {
                    $("#div_ajax_load_image").hide();
                }
            });
        }
        else {
            alert("작업을 완료해 주세요.");
        }
    });
})

function unlock() {
    var result = '';
    result = result + '['
    for (var i = 0; i < s_stc_id.length; i++) {
        result = result + '{' +
            '"id" : ' + s_stc_id[i];
        if (i == s_stc_id.length - 1) {
            result = result + '}';
        }
        else {
            result = result + '},';
        }
    }
    result = result + ']';

    return result;
}
/*
function btn_cancel_click(){
    var result = unlock();

    evi_start_offset = [];
    evi_end_offset = [];
    evi_str = [];
    s_stc_id = [];
    feed = [];
    console.log(result);

    $.ajax({
        type: "POST",
        url: "http://143.248.135.216:18503/pl3/unlock_extr",
        contentType: "application/json",
        dataType: "json",
        data: result,
        success: function (data) {
            $('#tbl_body').empty();
            console.log(data);
        },
        failure: function (err_msg) {
            console.log('ouch!')
            console.log(err_msg);
        },
        beforeSend: function () {
            var width = 0;
            var height = 0;
            var left = 0;
            var top = 0;

            width = 50;
            height = 50;

            top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
            left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

            if ($("#div_ajax_load_image").length != 0) {
                $("#div_ajax_load_image").css({
                    "top": top + "px",
                    "left": left + "px"
                });
                $("#div_ajax_load_image").show();
            }
            else {
                $('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#ffffff; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="../ajax_loader4.gif" style="width:100px; height:100px;"></div>');
            }
        },
        complete: function () {
            $("#div_ajax_load_image").hide();
        }
    });
}
*/
$(document).ready(function () {
    $("#btn_cancel").click(function (event) {
        //btn_cancel_click();
    var result = unlock();

    evi_start_offset = [];
    evi_end_offset = [];
    evi_str = [];
    s_stc_id = [];
    feed = [];
    console.log(result);

    $.ajax({
        type: "POST",
        url: "http://143.248.135.216:18503/pl3/unlock_extr",
        contentType: "application/json",
        dataType: "json",
        data: result,
        success: function (data) {
            $('#tbl_body').empty();
            console.log(data);
        },
        failure: function (err_msg) {
            console.log('ouch!')
            console.log(err_msg);
        },
        beforeSend: function () {
            var width = 0;
            var height = 0;
            var left = 0;
            var top = 0;

            width = 50;
            height = 50;

            top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
            left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

            if ($("#div_ajax_load_image").length != 0) {
                $("#div_ajax_load_image").css({
                    "top": top + "px",
                    "left": left + "px"
                });
                $("#div_ajax_load_image").show();
            }
            else {
                //$('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#ffffff; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="ajax_loader4.gif" style="width:100px; height:100px;"></div>');
            }
        },
        complete: function () {
            $("#div_ajax_load_image").hide();
        }
    });

	/*
        var result = unlock();

        evi_start_offset = [];
        evi_end_offset = [];
        evi_str = [];
        s_stc_id = [];
        feed = [];
        console.log(result);

        $.ajax({
            type: "POST",
            url: "http://143.248.135.216:18503/pl3/unlock_extr",
            contentType: "application/json",
            dataType: "json",
            data: result,
            success: function (data) {
                $('#tbl_body').empty();
                console.log(data);
            },
            failure: function (err_msg) {
                console.log('ouch!')
                console.log(err_msg);
            },
            beforeSend: function () {
                var width = 0;
                var height = 0;
                var left = 0;
                var top = 0;

                width = 50;
                height = 50;

                top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
                left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

                if ($("#div_ajax_load_image").length != 0) {
                    $("#div_ajax_load_image").css({
                        "top": top + "px",
                        "left": left + "px"
                    });
                    $("#div_ajax_load_image").show();
                }
                else {
                    $('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#ffffff; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="../ajax_loader4.gif" style="width:100px; height:100px;"></div>');
                }
            },
            complete: function () {
                $("#div_ajax_load_image").hide();
            }
        });
        */
    });
})

function working_check() {
    for (var i = 0; i < s_stc_id.length; i++) {
        if (feed[i] == null) {
            return 1;
        }
    }
    return 0;
}
/*
 $scope.loadSample = function () {
 if (loadSampleClickable) {
 loadSampleClickable = false;
 $http({
 url: "http://143.248.135.147:18512/pl3/samp_extr",
 method: "POST",
 data: {"p": $scope.property}
 }).success(function (extrObj) {
 console.log(extrObj);
 $scope.workingP = extrObj.p;
 $scope.extrArr = extrObj.samp;
 hideDivisions();
 $window.scrollTo(0, 0);
 $scope.labelDivStyle = {'visibility': 'visible', 'opacity': '1'};
 if ($scope.extrArr.length == 0) {
 alert("This property is done!")
 }
 });
 }


 }
 */
