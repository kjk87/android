package network.common;

import retrofit2.Call;

/**
 * Created by j2n on 2016. 9. 7..
 */
public interface PplusCallback<T>{

    void onResponse(Call<T> call, T response);

    void onFailure(Call<T> call, Throwable t, T response);
}
