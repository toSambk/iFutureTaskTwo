<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Древовидная структура</title>
    <style>

        body{
            font-family: 'Prompt', sans-serif;
            margin: 2em;
            font-size: 75%;
        }

        p {
            font-size: 1.2em;
        }
        a {
            color: #0066cc;
            text-decoration: none;
            outline: none;
        }
        a:hover {
            text-decoration: underline;
        }
        a:active, a:focus {
            color: #666;
            background-color: #f4f4f4;
        }
        a.current {
            color: black;
            font-weight: bold;
            background-color: #f4f4f4;
        }

        /* Дерево многоуровневое */
        #multi-derevo {
            width: 440px;
            border: solid;
            border-color: silver gray gray silver;
            border-width: 2px;
            padding: 0 0 1em 0;
            font-size: 1.3em;
        }

        #multi-derevo-copy {
            width: 440px;
            border: solid;
            border-color: silver gray gray silver;
            border-width: 2px;
            padding: 0 0 1em 0;
            font-size: 1.3em;
        }
        span {
            text-decoration: none;
            display: block;
            margin: 0 0 0 1.2em;
            background-color: transparent;
            border: solid silver;
            border-width: 0 0 1px 1px;
        }
        span a {
            display: block;
            position: relative;
            top: .95em;
            background-color: #fff;
            margin: 0 0 .2em .7em;
            padding: 0 0.3em;
        }
        h4 {
            font-size: 1em;
            font-weight: bold;
            margin: 0;
            padding: 0 .25em;
            border-bottom: 1px solid silver;
        }
        h4 a {
            display: block;
        }
        ul, li {
            list-style-image:none;
            list-style-position:outside;
            list-style-type:none;
            margin:0;
            padding:0;
        }
        ul li {
            line-height: 1.2em;
        }
        ul li ul {
            display: none;
        }
        ul li ul li {
            margin: 0 0 0 1.2em;
            border-left: 1px solid silver;
        }
        li.last {
            border: none;
        }
        .marker {
            border-color: transparent transparent transparent gray;
            border-style: solid;
            border-width: .25em 0 .25em .5em;
            margin: .35em .25em 0 0;
            float: left;
            width: 0px;
            height: 0px;
            line-height: 0px;
        }
        .marker.open {/* маркер раскрытия списка в открытом состоянии */
            border-color: gray transparent transparent transparent;
            border-width: .5em .25em 0 .25em;
        }

        .marker.spinner {
            color: black;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .marker.spinner:after {
            animation: changeContent .8s linear infinite;
            display: block;
            content: "⠋";
            font-size: 0.75em;
        }

        @keyframes changeContent {
            10% { content: "⠙"; }
            20% { content: "⠹"; }
            30% { content: "⠸"; }
            40% { content: "⠼"; }
            50% { content: "⠴"; }
            60% { content: "⠦"; }
            70% { content: "⠧"; }
            80% { content: "⠇"; }
            90% { content: "⠏"; }
        }

        * html #multi-derevo * { height: 1%;}
        * html #multi-derevo-copy * { height: 1%;}
        * html .marker { border-style: dotted dotted dotted solid; }
        * html .marker.open { border-style: solid dotted dotted dotted; }

        .dropdown-content {
            display: none;
            position: absolute;
            background-color: #f1f1f1;
            min-width: 100px;
            overflow: auto;
            box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
            z-index: 1;
        }

        .dropdown-content a {
            color: black;
            padding: 12px 16px;
            text-decoration: none;
            display: block;
        }

        .dropdown a:hover {background-color: #ddd;}

        .show {display: block;}
        /*Стили дроплиста*/

        /*Стили модального окна выбора узла*/
        @import 'https://fonts.googleapis.com/css?family=Prompt:400,700';

        .modal {
            display: block;
            width: 800px;/*600*/
            max-width: 100%;
            height: 600px;/*400*/
            max-height: 100%;
            position: fixed;
            z-index: 100;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            background: white;
            box-shadow: 0 0 60px 10px rgba(0, 0, 0, 0.9);
        }

        .closed {
            display: none;
        }

        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: 50;
            background: rgba(0, 0, 0, 0.6);
        }

        .modal-guts {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            padding: 20px 50px 20px 20px;
        }

        .modal .close-button {
            position: absolute;
            z-index: 1;
            top: 10px;
            right: 20px;
            border: 0;
            background: black;
            color: white;
            padding: 5px 10px;
            font-size: 1.3rem;
        }
        /*Стили модального окна выбора узла*/

    </style>
</head>

<body>

<div id="multi-derevo">
    <c:set var="menuitem" value="${node}" scope="request" />
    <h4><a href="#">Дерево компонентов</a></h4>
    <ul>
    <jsp:include page="nodeRecursionView.jsp" />
    </ul>
</div>

<div class="modal-overlay" id="modal-overlay" ></div>
<div class="modal" id="modal" aria-hidden="true" aria-labelledby="modalTitle" aria-describedby="modalDescription" role="dialog">
    <button class="close-button" id="close-button" title="Закрыть модальное окно">Закрыть</button>
    <div class="modal-guts" role="document">
        <h1>Выберите узел назначения</h1>
        <div id="multi-derevo-copy">
            <c:set var="copyitem" value="${node}" scope="request" />
            <h4><a href="#">Дерево выбора</a></h4>
            <ul>
                <jsp:include page="nodeParentToSelect.jsp" />
            </ul>
        </div>
    </div>
</div>

</body>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js" type="text/javascript"></script>
<script>

    var identificator = "default";
    var identificatorOfParentNodeToCopy = "default";
    var identificatorOfParentNodeToCut = "default";
    var modal = document.querySelector("#modal"),
        modalOverlay = document.querySelector("#modal-overlay"),
        closeButton = document.querySelector("#close-button"),
        closed = document.querySelector("#closed");
    var copyOperation = false;
    var cutOperation = false;

    $(document).ready(init());

    function init () {
        $('#multi-derevo li:has("ul")').find('a:first').prepend('<em class="marker"></em>');
        $('#multi-derevo li span').click(function () {
            $('a.current').removeClass('current');
            var a = $('a:first',this.parentNode);
            a.toggleClass('current');
            var li=$(this.parentNode);
            if (!li.next().length) {
                li.find('ul:first > li').addClass('last');
            }
            var ul=$('ul:first',this.parentNode);
            if (ul.length) {
                var em=$('em:first',this.parentNode);
                em.toggleClass('spinner');
                setTimeout(function() {
                    ul.slideToggle(200);
                    em.toggleClass('spinner');
                }, 2000);
                em.toggleClass('open');
            }
        });
        $('#multi-derevo-copy li:has("ul")').find('a:first').prepend('<em class="marker"></em>');
        $('#multi-derevo-copy li span').click(function () {
            $('a.current').removeClass('current');
            var a = $('a:first',this.parentNode);
            a.toggleClass('current');
            var li=$(this.parentNode);
            if (!li.next().length) {
                li.find('ul:first > li').addClass('last');
            }
            var ul=$('ul:first',this.parentNode);
            if (ul.length) {
                ul.slideToggle(200);
                var em=$('em:first',this.parentNode);
                em.toggleClass('open');
            }
        });
        modal.classList.toggle("closed");
        modalOverlay.classList.toggle("closed");
    }

    function basicDropdown(id) {
        document.getElementById("dropdownBasic").classList.toggle("show");
        identificator=id;
    }

    function copyDropdown(parentId) {
        document.getElementById("dropdownChangeAction").classList.toggle("show");
        identificatorOfParentNodeToCopy=parentId;
        identificatorOfParentNodeToCut=parentId;
    }

    function deleteItem(){
        var request = new XMLHttpRequest();
        request.open("GET", "${pageContext.request.contextPath}/delete?id="+identificator, false);
        request.send();
        window.location.reload();
    }

    function addItem(){
        var result = prompt("Введите имя узла", "Новый узел");
        if(result!=null) {
            var request = new XMLHttpRequest();
            request.open("GET", "${pageContext.request.contextPath}/add?id="+identificator+"&name="+result, false);
            request.send();
            window.location.reload();
        }
    }

    function selectNodeToChange() {
        var request = new XMLHttpRequest();
        if(copyOperation) {
            request.open("GET", "${pageContext.request.contextPath}/copy?id="+identificator+"&parent="+identificatorOfParentNodeToCopy, false);
            request.send();
        } else {
            request.open("GET", "${pageContext.request.contextPath}/cut?id="+identificator+"&parent="+identificatorOfParentNodeToCut, false);
            request.onload = function (){
                alert(request.responseText);
            }
            request.send();
        }
        window.location.reload();
    }

    function copyItem() {
        copyOperation = true;
        cutOperation = false;
        modal.classList.toggle("closed");
        modalOverlay.classList.toggle("closed");
    }

    function cutItem() {
        copyOperation = false;
        cutOperation = true;
        modal.classList.toggle("closed");
        modalOverlay.classList.toggle("closed");
    }

    closeButton.addEventListener("click", function() {
        modal.classList.toggle("closed");
        modalOverlay.classList.toggle("closed");
    });

    window.onclick = function(event) {
            var dropdowns = document.getElementsByClassName("dropdown-content");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
    }

</script>


