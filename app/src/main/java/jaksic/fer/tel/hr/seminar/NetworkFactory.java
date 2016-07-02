package jaksic.fer.tel.hr.seminar;

import retrofit.RestAdapter;

public class NetworkFactory {

    //Sa ovom metodom se stvara sučelje sa kojim ćeš pristupati podatcima na serveru.

    public static NetworkService create(Storage storage) {
        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(storage.getHttpAddress()).build();
        return restAdapter.create(NetworkService.class);
    }

}
