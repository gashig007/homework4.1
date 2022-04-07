package com.example.homework41;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.homework41.Model.Model;
import com.example.homework41.databinding.FragmentNewsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private Model model;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private final CollectionReference collectionReference = db.collection("news");
    private final DocumentReference reference = db.document("news/latest news");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = (Model) requireArguments().getSerializable("update");
        if (model != null) binding.edittext.setText(model.getTitle());

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edittext.getText().toString().isEmpty()){
                    YoYo.with(Techniques.Shake).duration(700).repeat(3).playOn(binding.edittext);
                }
                save();
            }
        });
    }

    private void save() {
        Bundle bundle = new Bundle();
        String text = binding.edittext.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(requireContext(), "type task!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (model == null) {
            model = new Model(System.currentTimeMillis(), text);
            App.getDataBase().newsDao().insert(model);
            saveToFirestore(model);
        }else {
            model.setTitle(text);
        }

        bundle.putSerializable("model", model);
        getParentFragmentManager().setFragmentResult("rk_news", bundle);
        close();
    }

    private void saveToFirestore(Model news) {
        db.collection("news")
                .add(news)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
        close();
    }
    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}