package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.whatsappandroid.cursoandroid.whatsapp.fragment.ContatosFragment;
import br.com.whatsappandroid.cursoandroid.whatsapp.fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter { //o FragmentStatePagerAdapter é mais voltado para fragmentos que nao sao staticos, que usam listagem de dados etc
    //já o FragmentPagerAdapter é mais para fragmentos staticos e mais simples
    //FragmentStatePagerAdapter trata os conteudos (elementos/ paginas) do viewpager como fragmentos

    private String[] tituloAbas = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //retorna para  a pagina (para o viewpager) os fragmentos

        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new ConversasFragment();
                break;

            case 1:
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        //retorna a quantidade de abas que queremos
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //recupera os titulos de cada uma das abas
        return tituloAbas[position];
    }
}
