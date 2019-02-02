package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import app.igaze.igaze.R;

/**
 * Created by
 *
 * @author Sarfaraz Iraqui
 * @email sarfarazghlm@gmail.com
 * on 4/1/19
 */
public class Reaction implements Parcelable {

    public static final String[] msgs = {"Happy to help", "Way to go", "Show of strength", "Haha"};
    public static final int[][] icons = {{R.drawable.ic_happytohelp_colored, R.drawable.ic_happytohelp_nocolor, R.drawable.ic_haha_nocolor_red_dot},
            {R.drawable.ic_waytogo_colored, R.drawable.ic_waytogo_nocolor, R.drawable.ic_waytogo_nocolor_red_dot},
            {R.drawable.ic_showofstrength_colored, R.drawable.ic_showofstrength_nocolor, R.drawable.ic_showofstrength_nocolor_red_dot},
            {R.drawable.ic_haha_colored, R.drawable.ic_haha_nocolor, R.drawable.ic_haha_nocolor_red_dot}};
    public static final int ICON_COLORED = 0, ICON_NOCOLOR = 1, ICON_NOCOLOR_RED_DOT = 2;
    // Todo: Above static fields should be removed by getting required info from local database;

    private String id;
    private String wishId;
    private String userId; // igaze ID
    private String displayName;
    private long time;

    public Reaction(String id, String wishId, String userId, String displayName, long time) {
        this.id = id;
        this.wishId = wishId;
        this.userId = userId;
        this.displayName = displayName;
        this.time = time;
    }

    // Todo: replace with a call to database to get message of current id
    public String getMessage() {
        try {
            return Reaction.msgs[Integer.parseInt(id) - 1];
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return Reaction.msgs[Integer.parseInt(getDefaultId()) - 1];
        } catch (IndexOutOfBoundsException iobe) {
            iobe.printStackTrace();
            return Reaction.msgs[Integer.parseInt(getDefaultId()) - 1];
        }
    }

    // Todo: replace with call to database and fetch default id
    public static String getDefaultId() {
        return "3";
    }

    public static int getDefaultIconResource(int ICON_TYPE) {
        try {
            return icons[Integer.parseInt(Reaction.getDefaultId()) - 1][ICON_TYPE];
        } catch (ArrayIndexOutOfBoundsException e){
            return R.drawable.ic_reaction_default;
        }
    }

    /**
     * ICON_TYPE are second indices of icons array
     * Valid values are ICON_COLORED, ICON_NOCOLOR, ICON_NOCOLOR_RED_DOT
     * These too need to be replaced with database calls
     */
    public int getIconResource(int ICON_TYPE) {
        try {

            return icons[Integer.parseInt(id)][ICON_TYPE];
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            return R.drawable.ic_reaction_default;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWishId() {
        return wishId;
    }


    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }


    public long getTimeLong() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.wishId);
        dest.writeString(this.userId);
        dest.writeString(this.displayName);
    }

    protected Reaction(Parcel in) {
        this.id = in.readString();
        this.wishId = in.readString();
        this.userId = in.readString();
        this.displayName = in.readString();
    }

    public static final Creator<Reaction> CREATOR = new Creator<Reaction>() {
        @Override
        public Reaction createFromParcel(Parcel source) {
            return new Reaction(source);
        }

        @Override
        public Reaction[] newArray(int size) {
            return new Reaction[size];
        }
    };

    @Override
    public int hashCode() {
        return (id + wishId + userId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Reaction)) {
            return false;
        }
        Reaction r = (Reaction)obj;
        return id.equals(r.id) && wishId.equals(r.wishId) && userId.equals(userId);
    }

}
