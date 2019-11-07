package com.example.szimplaingressos.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private String NOME_ARQUiVO = "app.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String EMAIL_USUARIO_LOGADO = "email_usuario_logado";
    private final String SENHA_USUARIO_LOGADO = "senha_usuario_logado";

    public Preferencias(Context contextoParametro) {
        context = contextoParametro;
        preferences = context.getSharedPreferences(NOME_ARQUiVO, MODE);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String email, String senha) {
        editor.putString(EMAIL_USUARIO_LOGADO, email);
        editor.putString(SENHA_USUARIO_LOGADO, senha);
        editor.commit();


    }
    public String getEmailUsuarioLogado() {
        return preferences.getString(EMAIL_USUARIO_LOGADO, null);
    }
    public String getSenhaUsuarioLogado() {
        return preferences.getString(SENHA_USUARIO_LOGADO, null);
    }
}
