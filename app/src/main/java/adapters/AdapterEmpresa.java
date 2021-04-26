package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ifoodclone.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Empresa;

public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder> {

    private List<Empresa> empresas;

    public AdapterEmpresa(List<Empresa> empresas){
        this.empresas = empresas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_empresa, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Empresa empresa = empresas.get(position);
        holder.nomeEmpresa.setText(empresa.getNome());
        holder.categoria.setText(empresa.getCategoria() + " - ");
        holder.preco.setText(empresa.getTempo() + " min");
        holder.entrega.setText("R$ " + empresa.getTempo());

        String urlImagem = empresa.getUrlImagem();
        Picasso.get().load(urlImagem).into(holder.imagemEmpresa);

    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemEmpresa;
        TextView nomeEmpresa, categoria, preco, entrega;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.txtNomeEmpresa);
            categoria = itemView.findViewById(R.id.txtPais);
            entrega = itemView.findViewById(R.id.txtTempo);
            preco = itemView.findViewById(R.id.txtPreco);

            imagemEmpresa = itemView.findViewById(R.id.imgEmpresa);
        }
    }
}
