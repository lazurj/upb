package webapp;

import database.dto.User;
import database.dto.Util.DtoUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OfflineDecServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);

        if(loggedUser == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        File offlineDecFile = new File("/home/xbruce/zadanie3/files/OfflineDec.zip");
        response.setHeader("Content-disposition", "attachment; filename=" + offlineDecFile.getName());
        OutputStream outFile = response.getOutputStream();
        FileInputStream in = new FileInputStream(offlineDecFile);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0) {
            outFile.write(buffer, 0, length);
        }
        in.close();
        outFile.flush();
    }

}
