import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itda.R

class board_recycle:RecyclerView.Adapter<Holder>() {
    val  listData= mutableListOf<board_recycle>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_recycle,parent,false)
        return Holder(view)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
        class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
            fun setMemo(memo:Memo){

            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}
class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

}