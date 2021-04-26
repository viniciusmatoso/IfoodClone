package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.br.ifoodclone.R;

import java.util.List;

import model.Produto;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    // Criando os atributos
    private List<Produto> produtos;
    private Context context;

    // Criando a lista e context
    public AdapterProduto(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    // Criando a View e preenchendo a lista
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(itemLista);
    }

    // Recuperando os dados
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Produto produto = produtos.get(i);
        holder.nome.setText(produto.getNome());
        holder.descricao.setText(produto.getDescricao());
        holder.preco.setText("R$ " + produto.getPreco());
    }

    // Fazendo a contagem de produtos
    @Override
    public int getItemCount() {
        return produtos.size();
    }

    // Linkando os atributos aos elementos gráficos
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView descricao;
        TextView preco;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtNomeProduto);
            descricao = itemView.findViewById(R.id.txtDescricaoProduto);
            preco = itemView.findViewById(R.id.txtPrecoProduto);
        }
    }


}
