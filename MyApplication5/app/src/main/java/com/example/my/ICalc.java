package com.example.my;

import br.ufc.great.caos.api.offload.Offloadable;

public interface ICalc {

    @Offloadable(Offloadable.Offload.STATIC)
    public String getDatas();

    //Média da temṕeratura de todos os usuários
    @Offloadable(Offloadable.Offload.STATIC)
    public String mediaTempAllUsers (String packageName);
}
