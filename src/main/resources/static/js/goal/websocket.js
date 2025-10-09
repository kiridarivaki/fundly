var stompClient = null;

function subscribeToFeed(goalId, messageHandler) {
    if (stompClient) {
        const topic = `/topic/goal/${goalId}/feed`;

        stompClient.subscribe(topic, function (stompMessage) {
            const eventData = JSON.parse(stompMessage.body);
            messageHandler(eventData, true);
        });
    }
}

window.connect = function(goalId, callback){
    if (!goalId) {
        return;
    }

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('STOMP Connected: ' + frame);

        subscribeToFeed(goalId, callback);

    }, function(error) {
        console.error("STOMP Connection Error:", error);
        stompClient = null;
    });
}