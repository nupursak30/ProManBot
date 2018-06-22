var dbHelper = require('./databaseHelper.js');

module.exports.setupWebhookEndpoints = function(webserver){

    webserver.get('/setup', function(req, res) {
    var html=`
    <html>
    <head></head>
    <body>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
    <script src="https://trello.com/1/client.js?key=dc704b415a99935383246a00b5563e66"></script>
    <script>
        // url parsing code from https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js
        function urlParam(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
            if (results==null){
                return null;
            }
            else{
                return decodeURI(results[1]) || 0;
            }
        }
        
        var opts={
            name:"ProManBot",
            scope: { read: true, write: true, account: true },
            success:function(){
                var trelloToken = Trello.token();

                // Now send this password back to Bot for safe storage
                $.ajax({
                url:"setup",
                method: "POST",
                //dataType: "json",
                data: { token: trelloToken, nounce: urlParam('nounce') },
                }).done(function () {
                    window.close();
                });
        },
        error: function(){
            
        }
        };
        Trello.authorize(opts)
    </script>
    </body></html>
    `;
    res.send(html);
    });

    webserver.post('/setup', function(req, res) {
        var token = req.body.token;
        var nounce = req.body.nounce;
        var userid = global.OAUTH_NOUNCE[nounce][0];
        if(typeof(userid) !== "undefined"){
        
        dbHelper.setTrelloToken(userid,token,function(status){
            global.TRELLO_TOKEN_MAP[userid]=token;
            var temp = global.OAUTH_NOUNCE[nounce][1];
            delete global.OAUTH_NOUNCE[nounce];
            temp();
        })
        }
        res.send("OK");
    });

    webserver.get('/card-icon.png',function(req,res){
        res.sendFile(__dirname + '/card-icon.PNG');
    });

}