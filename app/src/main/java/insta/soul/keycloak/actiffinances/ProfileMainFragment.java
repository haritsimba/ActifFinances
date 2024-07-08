package insta.soul.keycloak.actiffinances;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserLogout;
import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.databinding.FragmentProfileMainBinding;
import insta.soul.keycloak.actiffinances.keycloak.services.SessionStorage;
import insta.soul.keycloak.actiffinances.keycloak.services.UserService;

public class ProfileMainFragment extends Fragment {

    private FragmentProfileMainBinding binding;
    private UserService userService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userService = new UserService();

        binding.profileDisconnectBtn.setOnClickListener(v -> doLogOut());

        return view;
    }

    private void doLogOut() {
        String refreshToken = SessionStorage.getInstance().getAccessToken().getRefreshToken();
        userService.userLogout(refreshToken, new UserLogout.UserLogoutCallback() {
            @Override
            public void oneUserLogout(ActionStatus.UserLogoutStatus logoutStatus) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
