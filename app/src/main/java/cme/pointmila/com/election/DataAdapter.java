package cme.pointmila.com.election;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cme.pointmila.com.R;
import cme.pointmila.com.election.models.MyCandidate;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    List<MyCandidate> myCandidates;
    static GridOnClick gridOnClick;
    private Context context;

    public DataAdapter(Context context, List<MyCandidate> myCandidates, GridOnClick gridOnClick) {
        this.myCandidates = myCandidates;
        this.context = context;
        this.gridOnClick = gridOnClick;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.election_grid_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int position) {

        viewHolder.tv_android.setText(myCandidates.get(position).getCandidateName());

        if (myCandidates.get(position).getCandidateProfilePicstring() != null && !myCandidates.get(position).getCandidateProfilePicstring().equals("")) {
            try {

                System.out.println("pos---->" + position);
                byte[] decodedString = Base64.decode(myCandidates.get(position).getCandidateProfilePicstring(), Base64.DEFAULT);

                Glide.with(context)
                        .load(decodedString)
                        .asBitmap()
                        .placeholder(R.drawable.doc)
                        .into(viewHolder.img_android);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                image.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 100, 100, false));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Picasso.with(context).load(my.get(i).getAndroid_image_url()).resize(240, 120).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return myCandidates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private ImageView img_android;

        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView) view.findViewById(R.id.autofittextview);
            img_android = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

}