package info.meysam.hivaadapter;

/**
 * Created by ashkan on 2/1/18.
 */

public interface ItemBinder {

    int getResourceId();
	void bindToHolder(ItemHolder binder, Object listener);

}