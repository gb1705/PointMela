package cme.pointmila.com.election;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cme.pointmila.com.R;
import cme.pointmila.com.election.models.MyCandidate;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    List<MyCandidate> myCandidates;
    static GridOnClick gridOnClick;

    public ImageAdapter(Context c, List<MyCandidate> myCandidates, GridOnClick gridOnClick) {
        mContext = c;
        this.myCandidates = myCandidates;
        this.gridOnClick = gridOnClick;
    }

    public int getCount() {
        return myCandidates.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.election_grid_item, null);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.autofittextview);
            holder.profileImageView = (ImageView) convertView.findViewById(R.id.thumbnail);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTextView.setText(myCandidates.get(position).getCandidateId() + "." + myCandidates.get(position).getCandidateName());
        setImage(holder.profileImageView, myCandidates.get(position).getCandidateProfilePicstring());
        holder.bind(holder.profileImageView, position, myCandidates.get(position));

        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView;
        ImageView profileImageView;

        public void bind(View v, final int position, final MyCandidate myCandidate) {
            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridOnClick.onGridClickForNav(v, position, myCandidate);
                }
            });
        }

    }

    public void setImage(ImageView image, String base64) {
        if (base64 != null && !base64.equals("")) {
            try {
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);

                Glide.with(mContext)
                        .load(decodedString)
                        .asBitmap()
                        .placeholder(R.drawable.doc)
                        .into(image);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                image.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 100, 100, false));


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Glide.with(mContext)
                    .load("")
                    .asBitmap()
                    .placeholder(R.drawable.doc)
                    .into(image);

        }

    }
}

