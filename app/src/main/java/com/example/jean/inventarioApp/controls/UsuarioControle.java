package com.example.jean.inventarioApp.controls;

import com.example.jean.inventarioApp.model.Usuario;
import com.example.jean.inventarioApp.services.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsuarioControle {

    public int salvar(Usuario usuario){
        Map<String, Object> user = new HashMap<>();
        user.put("email", usuario.getEmail());
        user.put("usuario", usuario.getNome());
        user.put("id", usuario.getId());
        return 1;
    }
}
