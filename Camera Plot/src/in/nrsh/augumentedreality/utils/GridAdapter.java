package in.nrsh.augumentedreality.utils;

import in.nrsh.augumentedreality.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends ArrayAdapter<String> {

	private Context context;
	private String[] places;

	public GridAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
		places = context.getResources().getStringArray(R.array.places);
	}

	@Override
	public int getCount() {
		return places.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		Holder holder;
		// reuse views
		if (rowView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.grid_item, null);
			// configure view holder
			holder = new Holder();
			holder.txtView = (TextView) rowView.findViewById(R.id.txtView);
			holder.imgView = (ImageView) rowView.findViewById(R.id.imgView);
			rowView.setTag(holder);
		} else {
			holder = (Holder) rowView.getTag();
		}

		switch (position) {
		case 0:
			holder.imgView.setImageResource(R.drawable.airport);
			break;
		case 1:
			holder.imgView.setImageResource(R.drawable.amusement_park);
			break;
		case 2:
			holder.imgView.setImageResource(R.drawable.atm);
			break;
		case 3:
			holder.imgView.setImageResource(R.drawable.bank);
			break;
		case 4:
			holder.imgView.setImageResource(R.drawable.bus_stop);
			break;
		case 5:
			holder.imgView.setImageResource(R.drawable.hospital);
			break;
		}

		holder.txtView.setText(places[position]);
		
//		rowView.setBackgroundColor(position % 4 == 0 ? Color.BLUE
//				: ((position % 2 == 0) ? Color.GRAY
//						: (position % 4 == 1) ? Color.BLACK : Color.RED));
		
		return rowView;
	}

	static class Holder {
		TextView txtView;
		ImageView imgView;
	}

}
