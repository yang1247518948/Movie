package com.example.y1247.movie.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.y1247.movie.data.Movie;
import com.example.y1247.movie.data.source.LoadSourceType;
import com.example.y1247.movie.data.source.MovieValues;
import com.example.y1247.movie.data.source.MoviesDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by y1247 on 2017/3/8.
 */

public class MoviesLocalDataSource implements MoviesDataSource {

    private static MoviesLocalDataSource INSTANCE;

    private ContentResolver mContentResolver;

    private MoviesLocalDataSource(@NonNull ContentResolver contentResolver) {
        checkNotNull(contentResolver);
        mContentResolver = contentResolver;
    }

    public static MoviesLocalDataSource getInstance(@NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesLocalDataSource(contentResolver);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(@NonNull GetMoviesCallback callback, LoadSourceType extras,int page) {
        List<Movie> ls = new ArrayList<>();
        Cursor c = mContentResolver.query(MoviesPersistenceContract.MovieEntry.buildMoviesUri(),null,null,null,null);
        if (c != null) {
            while (c.moveToNext()) {
                ls.add(Movie.from(c));
            }
        }
        callback.onMoviesLoaded(ls);
    }

    @Override
    public void getMovie(@NonNull String id, @NonNull GetMovieCallback callback) {

    }

    @Override
    public void saveMovie(@NonNull Movie movie) {
        checkNotNull(movie);

        ContentValues values = MovieValues.from(movie);
        mContentResolver.insert(MoviesPersistenceContract.MovieEntry.buildMoviesUri(),values);
    }

    @Override
    public void saveMovies(@NonNull List<Movie> movies) {
        if (movies != null) {
            for (Movie temp : movies) {
                saveMovie(temp);
            }
        }
    }

    @Override
    public void collectMovie(@NonNull String id) {
        checkNotNull(id);

        ContentValues values = new ContentValues();
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_SAVE_FLAG,1);

        String selection = MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {id};

        int rows = mContentResolver.update(MoviesPersistenceContract.MovieEntry.buildMoviesUri(),values,selection,selectionArgs);

        checkNotNull(rows);
    }

    @Override
    public void unCollectMovie(@NonNull String id) {
        checkNotNull(id);

        ContentValues values = new ContentValues();
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_NAME_SAVE_FLAG,0);

        String selection = MoviesPersistenceContract.MovieEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {id};

        int rows = mContentResolver.update(MoviesPersistenceContract.MovieEntry.buildMoviesUri(),values,selection,selectionArgs);

        checkNotNull(rows);
    }

    @Override
    public void deleteAllMovies() {
        mContentResolver.delete(MoviesPersistenceContract.MovieEntry.buildMoviesUri(), null, null);
    }

    @Override
    public void refreshAll() {

    }

    @Override
    public void getReviews(@NonNull String id, @NonNull GetReviewCallback callback) {

    }

    @Override
    public void getVideos(@NonNull String id, @NonNull GetVideoCallback callback) {

    }
}
