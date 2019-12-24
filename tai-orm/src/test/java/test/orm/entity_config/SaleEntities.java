package test.orm.entity_config;

import com.google.common.collect.ImmutableList;
import tai.criteria.operators.JoinType;
import tai.orm.entity.*;
import tai.orm.entity.columnmapping.ColumnMapping;
import tai.orm.entity.columnmapping.DirectRelationMappingImpl;
import tai.orm.entity.columnmapping.RelationMapping;
import tai.orm.entity.columnmapping.VirtualRelationMappingImpl;
import test.orm.entities.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sohan on 6/25/2017.
 */
public interface SaleEntities {
    //Entities
    String USER_ENTITY = "User";
    String LEAVE_ENTITY = "Leave";
    String ATTENDANCE_ENTITY = "Attendance";
    String POSITION_REPORT_ENTITY = "PositionReport";
    String OUTLET_ENTITY = "Outlet";
    String PRODUCT_ENTITY = "Product";
    String PRODUCT_VARIATION_ENTITY = "ProductVariation";
    String SALE_ENTITY = "Sale";
    String SALE_ITEM_ENTITY = "SaleItem";
    String SR_ENTITY = "SR";
    String STOCK_ENTITY = "Stock";
    String STOCK_ITEM_ENTITY = "StockItem";
    String TARGET_ENTITY = "Target";
    String REGION_ENTITY = "Region";
    String AREA_ENTITY = "Area";
    String DISTRIBUTION_HOUSE_ENTITY = "DistributionHouse";
    String OUTLET_POSITION_ENTITY = "OutletPosition";
    String ATTENDANCE_SETTINGS_ENTITY = "AttendanceSettings";
    String TERRITORY_MASTER_ENTITY = "TerritoryMaster";
    String TERRITORY_ENTITY = "Territory";
    String OUTLET_TYPE_ENTITY = "OutletType";
    //Tables
    String USER_TABLE = "users";
    String ATTENDANCE_TABLE = "attendances";
    String POSITION_REPORT_TABLE = "position_reports";
    String OUTLET_TABLE = "outlets";
    String PRODUCT_TABLE = "products";
    String PRODUCT_VARIATION_TABLE = "product_variations";
    String SALE_TABLE = "sales";
    String SALE_ITEM_TABLE = "sale_items";
    String SR_TABLE = "srs";
    String STOCK_TABLE = "stocks";
    String STOCK_ITEM_TABLE = "stock_items";
    String TARGET_TABLE = "targets";
    String AREA_TABLE = "areas";
    String DISTRIBUTION_HOUSE_TABLE = "distribution_houses";
    String OUTLET_POSITION_TABLE = "outlet_positions";
    String REGION_TABLE = "regions";
    String LEAVE_TABLE = "leaves";
    String ATTENDANCE_SETTINGS_TABLE = "attendance_settings";
    String TERRITORY_MASTER_TABLE = "territory_masters";
    String TERRITORY_TABLE = "territories";
    String OUTLET_TYPE_TABLE = "outlet_types";

    String TARGET_ITEM_ENTITY = "TargetItem";
    String TARGET_ITEM_TABLE = "target_items";

    String DISTRICT_ENTITY = "District";
    String DISTRICT_TABLE = "districts";

    String SEC_TYPE_ENTITY = "SecType";
    String SEC_TYPE_TABLE = "sec_types";

    String FOE_ENTITY = "Foe";
    String FOE_TABLE = "foes";

    String FOE_OUTLET_VISIT_ENTITY = "FoeOutletVisit";
    String FOE_OUTLET_VISIT_TABLE = "foe_outlet_visits";
    String FOM_ENTITY = "Fom";
    String FOM_TABLE = "foms";
    String FOE_LEAVE_ENTITY = "FoeLeave";
    String FOE_LEAVE_TABLE = "foe_leaves";
    String FOM_LEAVE_ENTITY = "FomLeave";
    String FOM_LEAVE_TABLE = "fom_leaves";
    String CUSTOMER_ENTITY = "Customer";
    String CUSTOMER_TABLE = "customers";
    String FOM_POSITION_REPORT_ENTITY = "FomPositionReport";
    String FOM_POSITION_REPORT_TABLE = "fom_position_reports";
    String IMEI_ENTITY = "Imei";
    String IMEI_TABLE = "imei";
    String STOCK_RECORD_ENTITY = "StockRecord";
    String STOCK_RECORD_TABLE = "stock_records";
    String STOCK_RECORD_ITEM_ENTITY = "StockRecordItem";
    String STOCK_RECORD_ITEM_TABLE = "stock_record_items";
    String SEC_REVIEW_ENTITY = "SecReview";
    String SEC_REVIEW_TABLE = "sec_reviews";
    String SEC_REVIEW_ITEM_ENTITY = "SecReviewItem";
    String SEC_REVIEW_ITEM_TABLE = "sec_review_items";

    String VIDEO_TUTORIAL_ENTITY = "VideoTutorial";
    String VIDEO_TUTORIAL_TABLE = "video_tutorials";

    String VIDEO_TUTORIAL_VIEW_ENTITY = "VideoTutorialView";
    String VIDEO_TUTORIAL_VIEW_TABLE = "video_tutorial_views";

    static List<Entity> entities() {

        return Arrays.asList(
            regionEntity(),
            areaEntity(),
            territoryMasterEntity(),
            territoryEntity(),
            districtEntity(),
            distributionHouseEntity(),
            outletTypeEntity(),
            outletEntity(),
            srEntity(),
            secTypeEntity(),
            attendanceEntity(),
            positionReportEntity(),
            fomPositionReportEntity(),
            productEntity(),
            productVariationEntity(),
            saleItemEntity(),
            saleEntity(),
            customerEntity(),
            stockEntity(),
            stockRecordEntity(),
            stockItemEntity(),
            stockRecordItemEntity(),
            targetEntity(),
            targetItemEntity(),
            outletPositionEntity(),
            userEntity(),
            leaveEntity(),
            foeLeaveEntity(),
            fomLeaveEntity(),
            foeEntity(),
            fomEntity(),
            officeTimeEntity(),
            foeOutletVisitEntity(),
            imeiEntity(),
            videoTutorialEntity(),
            videoTutorialViewEntity(),
            secReviewEntity(),
            secReviewItemEntity()
        );
    }

    static Entity territoryMasterEntity() {
        return new Entity(
            TERRITORY_MASTER_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(TerritoryMasterModel.name),
                    field(TerritoryMasterModel.area, new Relationship(
                        Relationship.Name.HAS_ONE,
                        AREA_ENTITY,
                        new Relationship.Options(false, false, false)
                    )),
                    field(TerritoryMasterModel.outlets, new Relationship(
                        Relationship.Name.HAS_MANY,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(TerritoryMasterModel.territories, new Relationship(
                        Relationship.Name.HAS_MANY,
                        TERRITORY_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                TERRITORY_MASTER_TABLE,
                BaseTable.id,
                "tm",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(TerritoryMasterModel.name, TerritoryMasterTable.name)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        TerritoryMasterModel.area,
                        AREA_TABLE, AREA_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(TerritoryMasterTable.area_id, AreaTable.id)),
                        JoinType.JOIN
                    ))
                    .add(new VirtualRelationMappingImpl(
                        TerritoryMasterModel.outlets,
                        OUTLET_TABLE, OUTLET_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(OutletTable.territory_master_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new VirtualRelationMappingImpl(
                        TerritoryMasterModel.territories,
                        TERRITORY_TABLE, TERRITORY_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(TerritoryTable.territory_master_id, BaseTable.id)),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity territoryEntity() {
        return new Entity(
            TERRITORY_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(TerritoryModel.name),
                    field(TerritoryModel.territoryMaster, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TERRITORY_MASTER_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                TERRITORY_TABLE,
                BaseTable.id,
                "territory",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(TerritoryModel.name, TerritoryTable.name)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        TerritoryModel.territoryMaster,
                        TERRITORY_MASTER_TABLE, TERRITORY_MASTER_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(TerritoryTable.territory_master_id, BaseTable.id)),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity districtEntity() {
        return new Entity(
            DISTRICT_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(DistrictModel.name),
                    field(DistrictModel.outlets, new Relationship(
                        Relationship.Name.HAS_MANY,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                DISTRICT_TABLE,
                BaseTable.id,
                "dist",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(DistrictModel.name, DistrictTable.name)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new VirtualRelationMappingImpl(
                        DistrictModel.outlets,
                        OUTLET_TABLE,
                        OUTLET_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(OutletTable.district_id, DistrictTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity outletTypeEntity() {
        return new Entity(
            OUTLET_TYPE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(OutletTypeModel.name)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                OUTLET_TYPE_TABLE,
                BaseTable.id,
                "outlet_type",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(OutletTypeModel.name, OutletTypeTable.name)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                baseRelationMappingsArray()
            )
        );
    }

    static Entity officeTimeEntity() {
        return new Entity(
            ATTENDANCE_SETTINGS_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(AttendanceSettingsModel.attendanceStartTime),
                    field(AttendanceSettingsModel.attendanceStopTime),
                    field(AttendanceSettingsModel.halfDayUpperHalfStartTime),
                    field(AttendanceSettingsModel.halfDayUpperHalfStopTime),
                    field(AttendanceSettingsModel.halfDayLowerHalfStartTime),
                    field(AttendanceSettingsModel.halfDayLowerHalfStopTime)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                ATTENDANCE_SETTINGS_TABLE,
                BaseTable.id,
                "attendance_settings",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(AttendanceSettingsModel.attendanceStartTime, AttendanceSettingsTable.attendance_start_time),
                        column(AttendanceSettingsModel.attendanceStopTime, AttendanceSettingsTable.attendance_stop_time),

                        column(AttendanceSettingsModel.halfDayUpperHalfStartTime, AttendanceSettingsTable.half_day_upper_half_start_time),
                        column(AttendanceSettingsModel.halfDayUpperHalfStopTime, AttendanceSettingsTable.half_day_upper_half_stop_time),

                        column(AttendanceSettingsModel.halfDayLowerHalfStartTime, AttendanceSettingsTable.half_day_lower_half_start_time),
                        column(AttendanceSettingsModel.halfDayLowerHalfStopTime, AttendanceSettingsTable.half_day_lower_half_stop_time)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                baseRelationMappingsArray()
            )
        );
    }

    static Entity foeOutletVisitEntity() {
        return new Entity(
            FOE_OUTLET_VISIT_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(FoeOutletVisitModel.visitDate),
                    field(FoeOutletVisitModel.indexingDate),
                    field(FoeOutletVisitModel.latitude),
                    field(FoeOutletVisitModel.longitude),
                    field(FoeOutletVisitModel.accuracy),
                    field(FoeOutletVisitModel.pictureUri),
                    field(FoeOutletVisitModel.clientSideDate),
                    field(FoeOutletVisitModel.geoAddress),

                    field(FoeOutletVisitModel.foe, new Relationship(
                        Relationship.Name.HAS_ONE,
                        FOE_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FoeOutletVisitModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                FOE_OUTLET_VISIT_TABLE,
                BaseTable.id,
                "foe_outlet_visit",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(FoeOutletVisitModel.visitDate, FoeOutletVisitTable.visit_date),
                        column(FoeOutletVisitModel.indexingDate, FoeOutletVisitTable.indexing_date),
                        column(FoeOutletVisitModel.latitude, FoeOutletVisitTable.latitude),
                        column(FoeOutletVisitModel.longitude, FoeOutletVisitTable.longitude),
                        column(FoeOutletVisitModel.accuracy, FoeOutletVisitTable.accuracy),
                        column(FoeOutletVisitModel.pictureUri, FoeOutletVisitTable.picture_uri),
                        column(FoeOutletVisitModel.clientSideDate, FoeOutletVisitTable.client_side_date),
                        column(FoeOutletVisitModel.geoAddress, FoeOutletVisitTable.geo_address)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        FoeOutletVisitModel.foe,
                        FOE_TABLE, FOE_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(FoeOutletVisitTable.foe_id, BaseTable.id)),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FoeOutletVisitModel.outlet,
                        OUTLET_TABLE, OUTLET_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(FoeOutletVisitTable.outlet_id, BaseTable.id)),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity foeLeaveEntity() {
        return new Entity(
            FOE_LEAVE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(FoeLeaveModel.leaveDate),
                    field(FoeLeaveModel.leaveType),
                    field(FoeLeaveModel.remarks),
                    field(FoeLeaveModel.active),
                    field(FoeLeaveModel.foe, new Relationship(
                        Relationship.Name.HAS_ONE,
                        FOE_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FoeLeaveModel.authorizedBy, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FoeLeaveModel.territoryMaster, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TERRITORY_MASTER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FoeLeaveModel.foeOutletVisit, new Relationship(
                        Relationship.Name.HAS_ONE,
                        FOE_OUTLET_VISIT_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                FOE_LEAVE_TABLE,
                BaseTable.id,
                "foe_lv",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(FoeLeaveModel.leaveDate, FoeLeaveTable.leave_date),
                        column(FoeLeaveModel.leaveType, FoeLeaveTable.leave_type),
                        column(FoeLeaveModel.remarks, FoeLeaveTable.remarks),
                        column(FoeLeaveModel.active, FoeLeaveTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        FoeLeaveModel.foe,
                        FOE_TABLE, FOE_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FoeLeaveTable.foe_id, FoeTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FoeLeaveModel.authorizedBy,
                        USER_TABLE, USER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FoeLeaveTable.authorized_by, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FoeLeaveModel.territoryMaster,
                        TERRITORY_MASTER_TABLE, TERRITORY_MASTER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FoeLeaveTable.territory_master_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FoeLeaveModel.foeOutletVisit,
                        FOE_OUTLET_VISIT_TABLE, FOE_OUTLET_VISIT_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FoeLeaveTable.foe_outlet_visit_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(
                        baseRelationMappingsArray()
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity fomLeaveEntity() {
        return new Entity(
            FOM_LEAVE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(FomLeaveModel.leaveDate),
                    field(FomLeaveModel.leaveType),
                    field(FomLeaveModel.remarks),
                    field(FoeLeaveModel.active),
                    field(FomLeaveModel.fom, new Relationship(
                        Relationship.Name.HAS_ONE,
                        FOM_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FomLeaveModel.authorizedBy, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FomLeaveModel.area, new Relationship(
                        Relationship.Name.HAS_ONE,
                        AREA_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FomLeaveModel.presentLocation, new Relationship(
                        Relationship.Name.HAS_ONE,
                        FOM_POSITION_REPORT_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                FOM_LEAVE_TABLE,
                BaseTable.id,
                "fom_lv",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(FomLeaveModel.leaveDate, FomLeaveTable.leave_date),
                        column(FomLeaveModel.leaveType, FomLeaveTable.leave_type),
                        column(FomLeaveModel.remarks, FomLeaveTable.remarks),
                        column(FomLeaveModel.active, FomLeaveTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        FomLeaveModel.fom,
                        FOM_TABLE, FOM_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FomLeaveTable.fom_id, FoeTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FomLeaveModel.authorizedBy,
                        USER_TABLE, USER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FomLeaveTable.authorized_by, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FomLeaveModel.area,
                        AREA_TABLE, AREA_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FomLeaveTable.area_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FomLeaveModel.presentLocation,
                        FOM_POSITION_REPORT_TABLE, FOM_POSITION_REPORT_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FomLeaveTable.present_location_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity leaveEntity() {
        return new Entity(
            LEAVE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(LeaveModel.leaveDate),
                    field(LeaveModel.leaveType),
                    field(LeaveModel.remarks),
                    field(LeaveModel.halfDayType),
                    field(LeaveModel.active),
                    field(LeaveModel.sr, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SR_ENTITY,
                        new Relationship.Options()
                    )),
                    field(LeaveModel.authorizedBy, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(LeaveModel.attendance, new Relationship(
                        Relationship.Name.HAS_ONE,
                        ATTENDANCE_ENTITY,
                        new Relationship.Options()
                    )),
                    field(LeaveModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                LEAVE_TABLE,
                BaseTable.id,
                "lv",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(LeaveModel.leaveDate, LeaveTable.leave_date),
                        column(LeaveModel.leaveType, LeaveTable.leave_type),
                        column(LeaveModel.remarks, LeaveTable.remarks),
                        column(LeaveModel.halfDayType, LeaveTable.half_day_type),
                        column(LeaveModel.active, LeaveTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        PositionReportModel.sr,
                        SR_TABLE, SR_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(LeaveTable.sr_id, SRTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        LeaveModel.authorizedBy,
                        USER_TABLE, USER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(LeaveTable.authorized_by, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        LeaveModel.attendance,
                        ATTENDANCE_TABLE, ATTENDANCE_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(LeaveTable.attendance_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        LeaveModel.outlet,
                        OUTLET_TABLE, OUTLET_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(LeaveTable.outlet_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity foeEntity() {
        return new Entity(
            FOE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(FoeModel.dayOff),
                    field(FoeModel.user, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FoeModel.territoryMaster, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TERRITORY_MASTER_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                FOE_TABLE,
                BaseTable.id,
                "foe",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(FoeModel.dayOff, FoeTable.day_off)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        FoeModel.user,
                        USER_TABLE, USER_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(FoeTable.user_id, UserTable.id)),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FoeModel.territoryMaster,
                        TERRITORY_MASTER_TABLE, TERRITORY_MASTER_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(FoeTable.territory_master_id, BaseTable.id)),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity fomEntity() {
        return new Entity(
            FOM_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(FomModel.dayOff),
                    field(FoeModel.user, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FomModel.area, new Relationship(
                        Relationship.Name.HAS_ONE,
                        AREA_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                FOM_TABLE,
                BaseTable.id,
                "fom",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(FomModel.dayOff, FomTable.day_off)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        FoeModel.user,
                        USER_TABLE, USER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FomTable.user_id, UserTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FomModel.area,
                        AREA_TABLE, AREA_ENTITY,
                        ImmutableList.of(new ForeignColumnMapping(FomTable.area_id, BaseTable.id)),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity userEntity() {
        return new Entity(
            USER_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    new Field(UserModel.username, null),
                    new Field(UserModel.password, null),

                    new Field(UserModel.firstName, null),
                    new Field(UserModel.lastName, null),
                    new Field(UserModel.email, null),
                    new Field(UserModel.phone, null),

                    new Field(UserModel.dateOfBirth, null),
                    new Field(UserModel.joinDate, null),
                    new Field(UserModel.gender, null),
                    field(UserModel.pictureUri),
                    field(UserModel.userType),
                    field(UserModel.address),
                    field(UserModel.active)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                USER_TABLE,
                BaseTable.id,
                "user",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(UserModel.username, UserTable.username),
                        column(UserModel.password, UserTable.password),

                        column(UserModel.firstName, UserTable.first_name),
                        column(UserModel.lastName, UserTable.last_name),
                        column(UserModel.email, UserTable.email),
                        column(UserModel.phone, UserTable.phone),
                        column(UserModel.joinDate, UserTable.join_date),

                        column(UserModel.dateOfBirth, UserTable.date_of_birth),
                        column(UserModel.gender, UserTable.gender),
                        column(UserModel.pictureUri, UserTable.picture_uri),
                        column(UserModel.userType, UserTable.user_type),
                        column(UserModel.address, UserTable.address),
                        column(UserModel.active, UserTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                baseRelationMappingsArray()
            )
        );
    }

    static Entity attendanceEntity() {
        return new Entity(
            ATTENDANCE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(AttendanceModel.sr, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SR_ENTITY,
                        new Relationship.Options()
                    )),
                    field(AttendanceModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),

                    field(AttendanceModel.present),
                    field(AttendanceModel.presentDate),
                    field(AttendanceModel.deviceDateTime),
                    field(AttendanceModel.indexingDate),
                    field(AttendanceModel.presentLocation, new Relationship(
                        Relationship.Name.HAS_ONE,
                        POSITION_REPORT_ENTITY,
                        new Relationship.Options()
                    )),

                    field(AttendanceModel.pictureUri),
                    field(AttendanceModel.pictureUpdateDate)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                ATTENDANCE_TABLE,
                BaseTable.id,
                "attendance",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(AttendanceModel.present, AttendanceTable.present),
                        column(AttendanceModel.presentDate, AttendanceTable.present_date),
                        column(AttendanceModel.deviceDateTime, AttendanceTable.device_date_time),
                        column(AttendanceModel.indexingDate, AttendanceTable.indexing_date),

                        column(AttendanceModel.pictureUri, AttendanceTable.picture_uri),
                        column(AttendanceModel.pictureUpdateDate, AttendanceTable.picture_update_date)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        AttendanceModel.sr,
                        SR_TABLE, SR_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(
                                AttendanceTable.sr_id, SRTable.id
                            )
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        AttendanceModel.outlet,
                        OUTLET_TABLE, OUTLET_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(
                                AttendanceTable.outlet_id, BaseTable.id
                            )
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        AttendanceModel.presentLocation,
                        POSITION_REPORT_TABLE, POSITION_REPORT_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(
                                AttendanceTable.present_location_id, PositionReportTable.id
                            )
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity positionReportEntity() {
        return new Entity(
            POSITION_REPORT_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(PositionReportModel.sr, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SR_ENTITY,
                        new Relationship.Options()
                    )),
                    field(PositionReportModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(PositionReportModel.latitude),
                    field(PositionReportModel.longitude),
                    field(PositionReportModel.accuracy),
                    field(PositionReportModel.time)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                POSITION_REPORT_TABLE,
                BaseTable.id,
                "position_report",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(PositionReportModel.latitude, PositionReportTable.latitude),
                        column(PositionReportModel.longitude, PositionReportTable.longitude),
                        column(PositionReportModel.accuracy, PositionReportTable.accuracy),
                        column(PositionReportModel.time, PositionReportTable.time)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        PositionReportModel.sr,
                        SR_TABLE, SR_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(PositionReportTable.sr_id, SRTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        PositionReportModel.outlet,
                        OUTLET_TABLE, OUTLET_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(PositionReportTable.outlet_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity fomPositionReportEntity() {
        return new Entity(
            FOM_POSITION_REPORT_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(FomPositionReportModel.fom, new Relationship(
                        Relationship.Name.HAS_ONE,
                        FOM_ENTITY,
                        new Relationship.Options()
                    )),
                    field(FomPositionReportModel.area, new Relationship(
                        Relationship.Name.HAS_ONE,
                        AREA_ENTITY,
                        new Relationship.Options()
                    )),
                    field(PositionReportModel.latitude),
                    field(PositionReportModel.longitude),
                    field(PositionReportModel.accuracy),
                    field(PositionReportModel.time)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                FOM_POSITION_REPORT_TABLE,
                BaseTable.id,
                "fom_position_report",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(PositionReportModel.latitude, PositionReportTable.latitude),
                        column(PositionReportModel.longitude, PositionReportTable.longitude),
                        column(PositionReportModel.accuracy, PositionReportTable.accuracy),
                        column(PositionReportModel.time, PositionReportTable.time)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        FomPositionReportModel.fom,
                        FOM_TABLE, FOM_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FomPositionReportTable.fom_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        FomPositionReportModel.area,
                        AREA_TABLE, AREA_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(FomPositionReportTable.area_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity productEntity() {
        return new Entity(
            PRODUCT_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(ProductModel.name),
                    field(ProductModel.unitPrice),
                    field(ProductModel.active),
                    field(ProductModel.isCurrentTarget),
                    field(ProductModel.productVariations, new Relationship(
                        Relationship.Name.HAS_MANY,
                        PRODUCT_VARIATION_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                PRODUCT_TABLE,
                BaseTable.id,
                "product",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(ProductModel.name, ProductTable.name),
                        column(ProductModel.unitPrice, ProductTable.unit_price),
                        column(ProductModel.isCurrentTarget, ProductTable.is_current_target),
                        column(ProductModel.active, ProductTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new VirtualRelationMappingImpl(
                        ProductModel.productVariations,
                        PRODUCT_VARIATION_TABLE,
                        PRODUCT_VARIATION_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(ProductVariationTable.product_id, ProductTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity productVariationEntity() {
        return new Entity(
            PRODUCT_VARIATION_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(ProductVariationModel.name),
                    field(ProductVariationModel.sku),
                    field(ProductVariationModel.active),
                    field(ProductVariationModel.product, new Relationship(
                        Relationship.Name.HAS_ONE,
                        PRODUCT_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                PRODUCT_VARIATION_TABLE,
                BaseTable.id,
                "product_variation",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(ProductVariationModel.name, ProductVariationTable.name),
                        column(ProductVariationModel.sku, ProductVariationTable.sku),
                        column(ProductVariationModel.active, ProductVariationTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        ProductVariationModel.product,
                        PRODUCT_TABLE,
                        PRODUCT_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(ProductVariationTable.product_id, ProductTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity regionEntity() {
        return new Entity(
            REGION_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(RegionModel.name),
                    field(RegionModel.areas, new Relationship(
                        Relationship.Name.HAS_MANY,
                        SaleEntities.AREA_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                REGION_TABLE,
                BaseTable.id,
                "region",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(RegionModel.name, RegionTable.name)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new VirtualRelationMappingImpl(
                            RegionModel.areas,
                            SaleEntities.AREA_TABLE, SaleEntities.AREA_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(AreaTable.region_id, RegionTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(baseRelationMappingsArray())
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity areaEntity() {
        return new Entity(
            AREA_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(AreaModel.name),
                    field(AreaModel.region, new Relationship(
                        Relationship.Name.HAS_ONE,
                        REGION_ENTITY,
                        new Relationship.Options()
                    )),
                    field(AreaModel.territoryMasters, new Relationship(
                        Relationship.Name.HAS_MANY,
                        SaleEntities.TERRITORY_MASTER_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                AREA_TABLE,
                BaseTable.id,
                "area",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(AreaModel.name, AreaTable.name)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        AreaModel.region,
                        REGION_TABLE, REGION_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(AreaTable.region_id, RegionTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new VirtualRelationMappingImpl(
                        AreaModel.territoryMasters,
                        SaleEntities.TERRITORY_MASTER_TABLE, SaleEntities.TERRITORY_MASTER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(TerritoryMasterTable.area_id, AreaTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity distributionHouseEntity() {
        return new Entity(
            DISTRIBUTION_HOUSE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(DistributionHouseModel.name),
                    field(DistributionHouseModel.region, new Relationship(
                        Relationship.Name.HAS_ONE,
                        REGION_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                DISTRIBUTION_HOUSE_TABLE,
                BaseTable.id,
                "dis_house",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(DistributionHouseModel.name, DistributionHouseTable.name)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        DistributionHouseModel.region,
                        REGION_TABLE, REGION_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(DistributionHouseTable.region_id, BaseTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity outletEntity() {
        return new Entity(
            OUTLET_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(OutletModel.name),
                    field(OutletModel.pictureUri),
                    field(OutletModel.address),
                    field(OutletModel.dayOff),
                    field(OutletModel.dmsCode),
                    field(OutletModel.active),
                    field(OutletModel.outletType, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_TYPE_ENTITY,
                        new Relationship.Options()
                    )),
                    field(OutletModel.territoryMaster, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TERRITORY_MASTER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(OutletModel.territory, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TERRITORY_ENTITY,
                        new Relationship.Options()
                    )),
                    field(OutletModel.district, new Relationship(
                        Relationship.Name.HAS_ONE,
                        DISTRICT_ENTITY,
                        new Relationship.Options()
                    )),
                    field(OutletModel.position, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_POSITION_ENTITY,
                        new Relationship.Options()
                    )),
                    field(OutletModel.srs, new Relationship(
                        Relationship.Name.HAS_MANY,
                        SR_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                OUTLET_TABLE,
                BaseTable.id,
                "outlet",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(OutletModel.name, OutletTable.name),
                        column(OutletModel.pictureUri, OutletTable.picture_uri),
                        column(OutletModel.address, OutletTable.address),
                        column(OutletModel.dayOff, OutletTable.day_off),
                        column(OutletModel.dmsCode, OutletTable.dms_code),
                        column(OutletModel.active, OutletTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            OutletModel.outletType,
                            OUTLET_TYPE_TABLE,
                            OUTLET_TYPE_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(OutletTable.outlet_type_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            OutletModel.territoryMaster,
                            TERRITORY_MASTER_TABLE,
                            TERRITORY_MASTER_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(OutletTable.territory_master_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            OutletModel.territory,
                            TERRITORY_TABLE,
                            TERRITORY_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(OutletTable.territory_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            OutletModel.district,
                            DISTRICT_TABLE,
                            DISTRICT_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(OutletTable.district_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            OutletModel.position,
                            OUTLET_POSITION_TABLE,
                            OUTLET_POSITION_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(OutletTable.outlet_position_id, OutletPositionTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new VirtualRelationMappingImpl(
                            OutletModel.srs,
                            SR_TABLE,
                            SR_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SRTable.outlet_id, OutletModel.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity saleEntity() {
        return new Entity(
            SALE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(SaleModel.territoryMaster, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TERRITORY_MASTER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SaleModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SaleModel.items, new Relationship(
                        Relationship.Name.HAS_MANY,
                        SALE_ITEM_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SaleModel.sr, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SR_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SaleModel.saleDate),
                    field(SaleModel.clientSideDate),
                    field(SaleModel.totalAmount),
                    field(SaleModel.totalQuantity),

                    field(SaleModel.customerName),
                    field(SaleModel.customerMobile),

                    field(SaleModel.gender),
                    field(SaleModel.age),
                    field(SaleModel.education),
                    field(SaleModel.occupation),

                    field(SaleModel.remarks),
                    field(SaleModel.remarksCompetition)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                SALE_TABLE,
                BaseTable.id,
                "sale",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(SaleModel.saleDate, SaleTable.sale_date),
                        column(SaleModel.clientSideDate, SaleTable.client_side_date),
                        column(SaleModel.totalAmount, SaleTable.total_amount),
                        column(SaleModel.totalQuantity, SaleTable.total_quantity),

                        column(SaleModel.customerName, SaleTable.customer_name),
                        column(SaleModel.customerMobile, SaleTable.customer_mobile),

                        column(SaleModel.gender, SaleTable.gender),
                        column(SaleModel.age, SaleTable.age),
                        column(SaleModel.education, SaleTable.education),
                        column(SaleModel.occupation, SaleTable.occupation),

                        column(SaleModel.remarks, SaleTable.remarks),
                        column(SaleModel.remarksCompetition, SaleTable.remarks_competition)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            SaleModel.territoryMaster,
                            TERRITORY_MASTER_TABLE,
                            TERRITORY_MASTER_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SaleTable.territory_master_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            SaleModel.outlet,
                            OUTLET_TABLE,
                            OUTLET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SaleTable.outlet_id, OutletTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            SaleModel.sr,
                            SR_TABLE,
                            SR_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SaleTable.sr_id, SRTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new VirtualRelationMappingImpl(
                            SaleModel.items,
                            SALE_ITEM_TABLE,
                            SALE_ITEM_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SaleItemTable.sale_id, SaleTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity saleItemEntity() {
        return new Entity(
            SALE_ITEM_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(SaleItemModel.sale, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SALE_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SaleItemModel.productVariation, new Relationship(
                        Relationship.Name.HAS_ONE,
                        PRODUCT_VARIATION_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SaleItemModel.quantity),
                    field(SaleItemModel.unitPrice),
                    field(SaleItemModel.billedAmount),
                    field(SaleItemModel.paidAmount),
                    field(SaleItemModel.barCode),
                    field(SaleItemModel.pictureUri)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                SALE_ITEM_TABLE,
                BaseTable.id,
                "sale_item",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(SaleItemModel.quantity, SaleItemTable.quantity),
                        column(SaleItemModel.unitPrice, SaleItemTable.unit_price),
                        column(SaleItemModel.billedAmount, SaleItemTable.billed_amount),
                        column(SaleItemModel.paidAmount, SaleItemTable.paid_amount),
                        column(SaleItemModel.barCode, SaleItemTable.bar_code),
                        column(SaleItemModel.pictureUri, SaleItemTable.picture_uri)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            SaleItemModel.productVariation,
                            PRODUCT_VARIATION_TABLE,
                            PRODUCT_VARIATION_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SaleItemTable.product_variation_id, ProductVariationTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            SaleItemModel.sale,
                            SALE_TABLE,
                            SALE_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SaleItemTable.sale_id, SaleTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity srEntity() {
        return new Entity(
            SR_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(SRModel.user, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SRModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SRModel.secType, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SEC_TYPE_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                SR_TABLE,
                BaseTable.id,
                "sr",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            SRModel.user,
                            USER_TABLE,
                            USER_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SRTable.user_id, UserTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            SRModel.outlet,
                            OUTLET_TABLE,
                            OUTLET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SRTable.outlet_id, OutletModel.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            SRModel.secType,
                            SEC_TYPE_TABLE,
                            SEC_TYPE_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(SRTable.sec_type_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity secTypeEntity() {
        return new Entity(
            SEC_TYPE_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(SecTypeModel.name)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                SEC_TYPE_TABLE,
                BaseTable.id,
                "sec_type",
                new ArrayBuilderImpl<ColumnMapping>()
                    .add(column(SecTypeModel.name, SecTypeTable.name))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity stockEntity() {
        return new Entity(
            STOCK_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(StockModel.totalAmount),
                    field(StockModel.totalQuantity),
                    field(StockModel.stockUpdateDate),
                    field(StockModel.status),
                    field(StockModel.remarks),
                    field(StockModel.statusUpdateDate),
                    field(StockModel.statusUpdatedBy),
                    field(StockModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(StockModel.items, new Relationship(
                        Relationship.Name.HAS_MANY,
                        STOCK_ITEM_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                STOCK_TABLE,
                BaseTable.id,
                "stock",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(StockModel.totalAmount, StockTable.total_amount),
                        column(StockModel.totalQuantity, StockTable.total_quantity),
                        column(StockModel.stockUpdateDate, StockTable.stock_update_date),
                        column(StockModel.status, StockTable.status),
                        column(StockModel.remarks, StockTable.remarks),
                        column(StockModel.statusUpdateDate, StockTable.status_update_date),
                        column(StockModel.statusUpdatedBy, StockTable.status_updated_by)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            StockModel.outlet,
                            OUTLET_TABLE,
                            OUTLET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockTable.outlet_id, OutletTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new VirtualRelationMappingImpl(
                            StockModel.items,
                            STOCK_ITEM_TABLE,
                            STOCK_ITEM_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockItemTable.stock_id, StockTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity stockRecordEntity() {
        return new Entity(
            STOCK_RECORD_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(StockRecordModel.totalAmount),
                    field(StockRecordModel.totalQuantity),
                    field(StockRecordModel.stockUpdateDate),
                    field(StockRecordModel.status),
                    field(StockRecordModel.remarks),
                    field(StockRecordModel.statusUpdateDate),
                    field(StockRecordModel.statusUpdatedBy),
                    field(StockRecordModel.captureDate),
                    field(StockRecordModel.indexingDate),

                    field(StockRecordModel.stock, new Relationship(
                        Relationship.Name.HAS_ONE,
                        STOCK_ENTITY,
                        new Relationship.Options()
                    )),
                    field(StockRecordModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(StockRecordModel.items, new Relationship(
                        Relationship.Name.HAS_MANY,
                        STOCK_RECORD_ITEM_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                STOCK_RECORD_TABLE,
                BaseTable.id,
                "stock_record",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(StockRecordModel.totalAmount, StockRecordTable.total_amount),
                        column(StockRecordModel.totalQuantity, StockRecordTable.total_quantity),
                        column(StockRecordModel.stockUpdateDate, StockRecordTable.stock_update_date),
                        column(StockRecordModel.status, StockRecordTable.status),
                        column(StockRecordModel.remarks, StockRecordTable.remarks),
                        column(StockRecordModel.statusUpdateDate, StockRecordTable.status_update_date),
                        column(StockRecordModel.statusUpdatedBy, StockRecordTable.status_updated_by),
                        column(StockRecordModel.captureDate, StockRecordTable.capture_date),
                        column(StockRecordModel.indexingDate, StockRecordTable.indexing_date)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            StockRecordModel.stock,
                            STOCK_TABLE,
                            STOCK_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockRecordTable.stock_id, StockTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            StockRecordModel.outlet,
                            OUTLET_TABLE,
                            OUTLET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockRecordTable.outlet_id, OutletTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new VirtualRelationMappingImpl(
                            StockRecordModel.items,
                            STOCK_RECORD_ITEM_TABLE,
                            STOCK_RECORD_ITEM_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockRecordItemTable.stock_record_id, StockRecordTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity stockItemEntity() {
        return new Entity(
            STOCK_ITEM_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(StockItemModel.stock, new Relationship(
                        Relationship.Name.HAS_ONE,
                        STOCK_ENTITY,
                        new Relationship.Options()
                    )),
                    field(StockItemModel.productVariation, new Relationship(
                        Relationship.Name.HAS_ONE,
                        PRODUCT_VARIATION_ENTITY,
                        new Relationship.Options()
                    )),
                    field(StockItemModel.quantity)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                STOCK_ITEM_TABLE,
                BaseTable.id,
                "stock_item",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(StockItemModel.quantity, StockItemTable.quantity)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            StockItemModel.stock,
                            STOCK_TABLE,
                            STOCK_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockItemTable.stock_id, StockTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            StockItemModel.productVariation,
                            PRODUCT_VARIATION_TABLE,
                            PRODUCT_VARIATION_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockItemTable.product_variation_id, ProductVariationTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity stockRecordItemEntity() {
        return new Entity(
            STOCK_RECORD_ITEM_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(StockRecordItemModel.stockRecord, new Relationship(
                        Relationship.Name.HAS_ONE,
                        STOCK_RECORD_ENTITY,
                        new Relationship.Options()
                    )),
                    field(StockRecordItemModel.productVariation, new Relationship(
                        Relationship.Name.HAS_ONE,
                        PRODUCT_VARIATION_ENTITY,
                        new Relationship.Options()
                    )),
                    field(StockRecordItemModel.quantity)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                STOCK_RECORD_ITEM_TABLE,
                BaseTable.id,
                "stock_record_item",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(StockRecordItemModel.quantity, StockRecordItemTable.quantity)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            StockRecordItemModel.stockRecord,
                            STOCK_RECORD_TABLE,
                            STOCK_RECORD_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockRecordItemTable.stock_record_id, StockTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            StockRecordItemModel.productVariation,
                            PRODUCT_VARIATION_TABLE,
                            PRODUCT_VARIATION_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(StockRecordItemTable.product_variation_id, ProductVariationTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity targetEntity() {
        return new Entity(
            TARGET_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(TargetModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(TargetModel.items, new Relationship(
                        Relationship.Name.HAS_MANY,
                        TARGET_ITEM_ENTITY,
                        new Relationship.Options()
                    )),
                    field(TargetModel.targetMonth),
                    field(TargetModel.targetYear),
                    field(TargetModel.totalAmount),
                    field(TargetModel.totalQuantity)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                TARGET_TABLE,
                BaseTable.id,
                "tgt",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(TargetModel.targetMonth, TargetTable.target_month),
                        column(TargetModel.targetYear, TargetTable.target_year),
                        column(TargetModel.totalAmount, TargetTable.total_amount),
                        column(TargetModel.totalQuantity, TargetTable.total_quantity)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            TargetModel.outlet,
                            OUTLET_TABLE,
                            OUTLET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(TargetTable.outlet_id, OutletTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new VirtualRelationMappingImpl(
                            TargetModel.items,
                            TARGET_ITEM_TABLE,
                            TARGET_ITEM_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(TargetItemTable.target_id, TargetTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity targetItemEntity() {
        return new Entity(
            TARGET_ITEM_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(TargetItemModel.product, new Relationship(
                        Relationship.Name.HAS_ONE,
                        PRODUCT_ENTITY,
                        new Relationship.Options()
                    )),
                    field(TargetItemModel.quantity),
                    field(TargetItemModel.target, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TARGET_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                TARGET_ITEM_TABLE,
                BaseTable.id,
                "tgt_item",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(TargetItemModel.quantity, TargetItemTable.quantity)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            TargetItemModel.product,
                            PRODUCT_TABLE,
                            PRODUCT_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(TargetItemTable.product_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            TargetItemModel.target,
                            TARGET_TABLE,
                            TARGET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(TargetItemTable.target_id, TargetTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity outletPositionEntity() {
        return new Entity(
            OUTLET_POSITION_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(OutletPositionModel.latitude),
                    field(OutletPositionModel.longitude),
                    field(OutletPositionModel.accuracy),
                    field(OutletPositionModel.time),
                    field(OutletPositionModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                OUTLET_POSITION_TABLE,
                BaseTable.id,
                "outlet_position",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(OutletPositionModel.latitude, OutletPositionTable.latitude),
                        column(OutletPositionModel.longitude, OutletPositionTable.longitude),
                        column(OutletPositionModel.accuracy, OutletPositionTable.accuracy),
                        column(OutletPositionModel.time, OutletPositionTable.time)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            OutletPositionModel.outlet,
                            OUTLET_TABLE,
                            OUTLET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(OutletPositionTable.outlet_id, OutletModel.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity customerEntity() {
        return new Entity(
            CUSTOMER_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(CustomerModel.territoryMaster, new Relationship(
                        Relationship.Name.HAS_ONE,
                        TERRITORY_MASTER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(CustomerModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(CustomerModel.sr, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SR_ENTITY,
                        new Relationship.Options()
                    )),
                    field(CustomerModel.saleDate),
                    field(CustomerModel.clientSideDate),

                    field(CustomerModel.customerName),
                    field(CustomerModel.customerMobile),

                    field(CustomerModel.gender),
                    field(CustomerModel.age),
                    field(CustomerModel.education),
                    field(CustomerModel.occupation),
                    field(CustomerModel.priceSegment),
                    field(CustomerModel.active)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                CUSTOMER_TABLE,
                BaseTable.id,
                "customer",
                new ArrayBuilderImpl<ColumnMapping>()
                    .addAll(Arrays.asList(
                        column(CustomerModel.saleDate, CustomerTable.sale_date),
                        column(CustomerModel.clientSideDate, CustomerTable.client_side_date),

                        column(CustomerModel.customerName, CustomerTable.customer_name),
                        column(CustomerModel.customerMobile, CustomerTable.customer_mobile),

                        column(CustomerModel.gender, CustomerTable.gender),
                        column(CustomerModel.age, CustomerTable.age),
                        column(CustomerModel.education, CustomerTable.education),
                        column(CustomerModel.occupation, CustomerTable.occupation),
                        column(CustomerModel.priceSegment, CustomerTable.price_segment),
                        column(CustomerModel.active, CustomerTable.active)
                    ))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(
                        new DirectRelationMappingImpl(
                            CustomerModel.territoryMaster,
                            TERRITORY_MASTER_TABLE,
                            TERRITORY_MASTER_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(CustomerTable.territory_master_id, BaseTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            CustomerModel.outlet,
                            OUTLET_TABLE,
                            OUTLET_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(CustomerTable.outlet_id, OutletTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .add(
                        new DirectRelationMappingImpl(
                            CustomerModel.sr,
                            SR_TABLE,
                            SR_ENTITY,
                            ImmutableList.of(
                                new ForeignColumnMapping(CustomerTable.sr_id, SRTable.id)
                            ),
                            JoinType.JOIN
                        )
                    )
                    .addAll(
                        ImmutableList.copyOf(baseRelationMappingsArray())
                    )
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity imeiEntity() {
        return new Entity(
            IMEI_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(ImeiModel.imei),
                    field(ImeiModel.active),
                    field(ImeiModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                IMEI_TABLE,
                BaseTable.id,
                "imei",
                new ArrayBuilderImpl<ColumnMapping>()
                    .add(column(ImeiModel.imei, ImeiTable.imei))
                    .add(column(ImeiModel.active, ImeiTable.active))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        ImeiModel.outlet,
                        OUTLET_TABLE, OUTLET_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(ImeiTable.outlet_id, OutletTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity videoTutorialEntity() {
        return new Entity(
            VIDEO_TUTORIAL_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(VideoTutorialModel.title),
                    field(VideoTutorialModel.description),
                    field(VideoTutorialModel.videoUrl),
                    field(VideoTutorialModel.previewPictureUrl),
                    field(VideoTutorialModel.active),
                    field(VideoTutorialModel.fileSize),
                    field(VideoTutorialModel.fileName)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                VIDEO_TUTORIAL_TABLE,
                BaseTable.id,
                "video_tutorial",
                new ArrayBuilderImpl<ColumnMapping>()
                    .add(column(VideoTutorialModel.title, VideoTutorialTable.title))
                    .add(column(VideoTutorialModel.description, VideoTutorialTable.description))
                    .add(column(VideoTutorialModel.videoUrl, VideoTutorialTable.video_url))
                    .add(column(VideoTutorialModel.previewPictureUrl, VideoTutorialTable.preview_picture_url))
                    .add(column(VideoTutorialModel.active, VideoTutorialTable.active))
                    .add(column(VideoTutorialModel.fileSize, VideoTutorialTable.file_size))
                    .add(column(VideoTutorialModel.fileName, VideoTutorialTable.file_name))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity videoTutorialViewEntity() {
        return new Entity(
            VIDEO_TUTORIAL_VIEW_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(VideoTutorialViewModel.viewerId),
                    field(VideoTutorialViewModel.user, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    )),
                    field(VideoTutorialViewModel.videoTutorial, new Relationship(
                        Relationship.Name.HAS_ONE,
                        VIDEO_TUTORIAL_ENTITY,
                        new Relationship.Options()
                    )),
                    field(VideoTutorialViewModel.viewDate),
                    field(VideoTutorialViewModel.viewDuration)
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                VIDEO_TUTORIAL_VIEW_TABLE,
                BaseTable.id,
                "video_tutorial_view",
                new ArrayBuilderImpl<ColumnMapping>()
                    .add(column(VideoTutorialViewModel.viewerId, VideoTutorialViewTable.viewer_id))
                    .add(column(VideoTutorialViewModel.viewDate, VideoTutorialViewTable.view_date))
                    .add(column(VideoTutorialViewModel.viewDuration, VideoTutorialViewTable.view_duration))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        VideoTutorialViewModel.user,
                        USER_TABLE, USER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(VideoTutorialViewTable.user_id, UserTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        VideoTutorialViewModel.videoTutorial,
                        VIDEO_TUTORIAL_TABLE, VIDEO_TUTORIAL_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(VideoTutorialViewTable.video_tutorial_id, VideoTutorialTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity secReviewItemEntity() {
        return new Entity(
            SEC_REVIEW_ITEM_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(SecReviewItemModel.reviewValue),
                    field(SecReviewItemModel.reviewType),
                    field(SecReviewItemModel.active),
                    field(SecReviewItemModel.secReview, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SEC_REVIEW_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                SEC_REVIEW_ITEM_TABLE,
                BaseTable.id,
                "sec_review_item",
                new ArrayBuilderImpl<ColumnMapping>()
                    .add(column(SecReviewItemModel.reviewValue, SecReviewItemTable.review_value))
                    .add(column(SecReviewItemModel.reviewType, SecReviewItemTable.review_type))
                    .add(column(SecReviewItemModel.active, SecReviewItemTable.active))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new DirectRelationMappingImpl(
                        SecReviewItemModel.secReview,
                        SEC_REVIEW_TABLE, SEC_REVIEW_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(SecReviewItemTable.sec_review_id, SecReviewTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static Entity secReviewEntity() {
        return new Entity(
            SEC_REVIEW_ENTITY,
            BaseModel.id,
            new ArrayBuilderImpl<Field>()
                .addAll(Arrays.asList(
                    field(SecReviewModel.reviewDate),
                    field(SecReviewModel.indexingDate),
                    field(SecReviewModel.reviewAverage),
                    field(SecReviewModel.active),
                    field(SecReviewModel.reviewItems, new Relationship(
                        Relationship.Name.HAS_MANY,
                        SEC_REVIEW_ITEM_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SecReviewModel.sec, new Relationship(
                        Relationship.Name.HAS_ONE,
                        SR_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SecReviewModel.outlet, new Relationship(
                        Relationship.Name.HAS_ONE,
                        OUTLET_ENTITY,
                        new Relationship.Options()
                    )),
                    field(SecReviewModel.givenBy, new Relationship(
                        Relationship.Name.HAS_ONE,
                        USER_ENTITY,
                        new Relationship.Options()
                    ))
                ))
                .addAll(baseFields())
                .build(list -> list.toArray(new Field[list.size()])),
            new DbMapping(
                SEC_REVIEW_TABLE,
                BaseTable.id,
                "sec_review",
                new ArrayBuilderImpl<ColumnMapping>()
                    .add(column(SecReviewModel.reviewDate, SecReviewTable.review_date))
                    .add(column(SecReviewModel.indexingDate, SecReviewTable.indexing_date))
                    .add(column(SecReviewModel.reviewAverage, SecReviewTable.review_average))
                    .add(column(SecReviewModel.active, SecReviewTable.active))
                    .addAll(baseColumns())
                    .build(list -> list.toArray(new ColumnMapping[list.size()])),
                new ArrayBuilderImpl<RelationMapping>()
                    .add(new VirtualRelationMappingImpl(
                        SecReviewModel.reviewItems,
                        SEC_REVIEW_ITEM_TABLE, SEC_REVIEW_ITEM_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(SecReviewItemTable.sec_review_id, SecReviewTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        SecReviewModel.sec,
                        SR_TABLE, SR_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(SecReviewTable.sec_id, SRTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        SecReviewModel.outlet,
                        OUTLET_TABLE, OUTLET_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(SecReviewTable.outlet_id, OutletTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .add(new DirectRelationMappingImpl(
                        SecReviewModel.givenBy,
                        USER_TABLE, USER_ENTITY,
                        ImmutableList.of(
                            new ForeignColumnMapping(SecReviewTable.given_by, UserTable.id)
                        ),
                        JoinType.JOIN
                    ))
                    .addAll(ImmutableList.copyOf(baseRelationMappingsArray()))
                    .build(list -> list.toArray(new RelationMapping[list.size()]))
            )
        );
    }

    static ColumnMapping column(String field, String column) {
        return new ColumnMapping(field, column);
    }

    static Field field(String name) {
        return new Field(name, null);
    }

    static Field field(String name, Relationship relationship) {
        return new Field(name, relationship);
    }

    static ArrayBuilder.CreateArrayFunc<ColumnMapping> toColumnsArray() {
        return list -> list.toArray(new ColumnMapping[list.size()]);
    }

    static ArrayBuilder.CreateArrayFunc<Field> toFieldsArray() {
        return list -> list.toArray(new Field[list.size()]);
    }

    static List<RelationMapping> baseRelationMappingsArray() {
        return Arrays.asList(
            new RelationMapping[]{
                createdBy(),
                updatedBy(),
            }
        );
    }

    static Field updatedByField() {
        return new Field(UserModel.updatedBy, new Relationship(
            Relationship.Name.HAS_ONE, USER_ENTITY, new Relationship.Options()
        ));
    }

    static Field createdByField() {
        return new Field(
            UserModel.createdBy,
            new Relationship(
            Relationship.Name.HAS_ONE,
            USER_ENTITY,
            new Relationship.Options()
        ));
    }

    static RelationMapping updatedBy() {
        return new DirectRelationMappingImpl(
            UserModel.updatedBy,
            USER_TABLE,
            USER_ENTITY,
            ImmutableList.of(
                new ForeignColumnMapping(UserTable.updated_by, UserTable.id)
            ),
            JoinType.JOIN
        );
    }

    static RelationMapping createdBy() {
        return new DirectRelationMappingImpl(
            UserModel.createdBy,
            USER_TABLE,
            USER_ENTITY,
            ImmutableList.of(
                new ForeignColumnMapping(UserTable.created_by, UserTable.id)
            ),
            JoinType.JOIN
        );
    }

    static List<Field> baseFields() {
        return Arrays.asList(
            new Field(UserModel.id, null),
            new Field(UserModel.createDate, null),
            new Field(UserModel.updateDate, null),
            createdByField(),
            updatedByField()
        );
    }

    static List<ColumnMapping> baseColumns() {
        return Arrays.asList(
            new ColumnMapping(UserModel.id, UserTable.id),
            new ColumnMapping(UserModel.createDate, UserTable.create_date),
            new ColumnMapping(UserModel.updateDate, UserTable.update_date)
        );
    }
}