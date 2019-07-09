package ir.moderndata.states;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class Locations extends BaseModel {

    @PrimaryKey(autoincrement = true)
    int Id;

    @Column
    int ProjectId;

    @Column
    int TeamId;

    @Column
    String LatLng;

}
