package com.example.book;

import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BooksFragment extends Fragment {

    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_books, container, false);
        // obiect retrofit configurat cu un URL de baza
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openlibrary.org/subjects/")
                .addConverterFactory(GsonConverterFactory.create()) // face maparea dintre JSON si modelul din Java
                .build();

        Api api = retrofit.create(Api.class);
        Call<BookResponse> call = api.getBooks();
        // call pe api pe un thread separat pentru a nu bloca UI
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, final Response<BookResponse> response) {
                RecyclerView recyclerView = v.findViewById(R.id.rw);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                BookAdapter adapter = new BookAdapter(getContext(), response.body().getWorks());
                adapter.setClickListener(new BookAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        NotificationFragment fragment = new NotificationFragment(response.body().getWorks().get(position).getTitle());
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.contentFragment, fragment);
                        transaction.commit();
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {

            }
        });

        return v;
    }
}
