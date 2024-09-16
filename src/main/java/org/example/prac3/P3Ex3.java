package org.example.prac3;

import io.reactivex.Observable;

public class P3Ex3 {
    private record UserFriend(Integer userId, Integer friendId) {}

    public Observable<UserFriend> getFriends(int userId) {
        return Observable.fromArray(
                new UserFriend(userId, (int) (Math.random() * 100)),
                new UserFriend(userId, (int) (Math.random() * 100)),
                new UserFriend(userId, (int) (Math.random() * 100))
        );
    }

    public void execute() {
        Integer[] userIdArray = {1, 2, 3, 4, 5};

        Observable<Integer> userIdStream = Observable.fromArray(userIdArray);
        Observable<UserFriend> userFriendStream = userIdStream.flatMap(userId -> getFriends(userId));
        userFriendStream.blockingSubscribe(userFriend -> System.out.println("User: " + userFriend.userId() + " Friend: " + userFriend.friendId()));
    }
}
