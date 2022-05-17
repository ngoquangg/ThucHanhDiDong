package ntu.bookman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase databaseBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseBook = SQLiteDatabase.openOrCreateDatabase("/data/data/ntu.bookman/MyBook.db", null);
        //Che hàm TaoBangVaThemDuLieu lại khi chạy ở các lần sau vì CSDL sẽ tạo lại từ đầu
        TaoBangVaThemDuLieu();
        NapSachvaoListview();
    }

    void ThemMoiSach (int ma, String ten, int sotrang, float gia, String mota){
        ContentValues row = new ContentValues();
        row.put("BookID",ma);
        row.put("BookName",ten);
        row.put("Page",sotrang);
        row.put("Price",gia);
        row.put("Description",mota);
        long kq = databaseBook.insert("BOOKS",null,row);
        if (kq == -1)
            Toast.makeText(this, "Khong them duoc", Toast.LENGTH_LONG).show();
        else
        {
            Toast.makeText(this, "Da them thanh cong", Toast.LENGTH_LONG).show();
        }
    }

    void XoaSach(int ma){
        String[] thamsSoTruyen= {String.valueOf(ma)};
        int kq = databaseBook.delete("BOOKS","BookID=?",thamsSoTruyen);
        if(kq == 0)
            Toast.makeText(this, "Không xóa được", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_LONG).show();
    }

    void CapNhap(int maGoc,String tenMoi, int sotrangMoi, float giaMoi, String motaMoi){
        String[] thamsSoTruyen = {String.valueOf(maGoc)};
        ContentValues row = new ContentValues();
        row.put("BookName", tenMoi);
        row.put("Page", sotrangMoi);
        row.put("Price", giaMoi);
        row.put("Description", motaMoi);
        int kq = databaseBook.update("BOOKS", row, "BookID=?", thamsSoTruyen);
        if (kq == 0)
            Toast.makeText(this, "Không cập nhập được", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Cập nhập thành công", Toast.LENGTH_LONG).show();
    }

    void NapSachvaoListview(){
        //Lấy tham chiếu đến Listview trong Layout
        ListView listView = (ListView) findViewById(R.id.lvSach);
        //Nguồn dữ liệu
        //Mỗi phần tử là 1 string, gồm: mã + tên + giá
        ArrayList<String> dsSach = new ArrayList<String>();
        //Mở DB, select dữ liêu đưa vào ArrayList
        //
        Cursor cs = databaseBook.rawQuery("SELECT * FROM BOOKS",null);
        cs.moveToFirst();
        //Duyệt từng bản ghi, tính toán
        while (true)
        {
            if(cs.isAfterLast()==true) break;
            //Lấy mã sách
            int ms = cs.getInt(0); //Cột 0 ở dòng hiện tại
            //Lấy tên sách
            String tenSach = cs.getString(1);
            //Lấy giá bán
            Float gia = cs.getFloat(3);
            //Nối lại để đưa vào ArrayList
            String dong = String.valueOf(ms) + "..." + tenSach + "..." + String.valueOf(gia);
            dsSach.add(dong);
            cs.moveToNext();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,dsSach);
        listView.setAdapter(adapter);
    }

    void TaoBangVaThemDuLieu() {
        //Lệnh tạo bảng
        //sqlXoaBang nếu đã có
        String sqlXoaBang ="DROP TABLE IF EXISTS BOOKS";
        databaseBook.execSQL(sqlXoaBang);
        String sqlTaoBang ="CREATE TABLE BOOKS(BookID integer PRIMARY KEY, " +
                "   BookName text, " +
                "   Page integer, "+
                "   Price Float, "+
                "   Description text)";
        databaseBook.execSQL(sqlTaoBang);
        //Them bản ghi
        String sqlThem1= "INSERT INTO BOOKS VALUES(1, 'Java', 100, 9.99, 'Sách về java')";
        databaseBook.execSQL(sqlThem1);
        String sqlThem2= "INSERT INTO BOOKS VALUES(2, 'Android', 320, 19.00, 'Android cơ bản')";
        databaseBook.execSQL(sqlThem2);
        String sqlThem3= "INSERT INTO BOOKS VALUES(3, 'Học làm giàu', 120, 0.99, 'sách đọc cho vui') ";
        databaseBook.execSQL(sqlThem3);
        String sqlThem4= "INSERT INTO BOOKS VALUES(4, 'Tử điển Anh-Việt', 1000, 29.50, 'Từ điển 100.000 từ')";
        databaseBook.execSQL(sqlThem4);
        String sqlThem5= "INSERT INTO BOOKS VALUES(5, 'CNXH', 1, 1, 'chuyện cổ tích')";
        databaseBook.execSQL(sqlThem5);
    }
}