package com.example.my;

import br.ufc.great.caos.api.offload.Offloadable;

public interface IRead {

    //Média da temperatura do usuário
    @Offloadable(Offloadable.Offload.STATIC)
    public String mediaTempAllUsers (String packageName);

    //Média dos batimentos do usuário
    @Offloadable(Offloadable.Offload.STATIC)
    public String mediaBatiUser (String packageName);

    //Média dos dez primeiros batimentos armazenados do usuário
    @Offloadable(Offloadable.Offload.STATIC)
    public String mediaDezPBatiUser (String packageName);

}
