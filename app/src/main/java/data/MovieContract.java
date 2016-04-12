package data;

import android.provider.BaseColumns;

/**
 * Created by ajvan on 12/04/2016.
 */
public final class MovieContract {

    public MovieContract() {
    }


    public static final class MovieEntry implements BaseColumns {
        /**
         * fields:
         * entry_id
         * original_title (String)
         * poster_image_name (String)
         * overview (String)
         * vote_average (float)
         * release_date (short)
         *
         */
        public static final String TABLE_NAME = "movie_data";
        public static final String COLUMN_NAME_ENTRY_ID = "entry_id";
    }
}
