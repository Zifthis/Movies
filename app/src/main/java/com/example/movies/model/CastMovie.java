package com.example.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastMovie implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    public final static Parcelable.Creator<CastMovie> CREATOR = new Creator<CastMovie>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CastMovie createFromParcel(Parcel in) {
            return new CastMovie(in);
        }

        public CastMovie[] newArray(int size) {
            return (new CastMovie[size]);
        }

    };

    protected CastMovie(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.cast, (Cast.class.getClassLoader()));
    }

    public CastMovie() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(cast);
    }

    public int describeContents() {
        return 0;
    }

}