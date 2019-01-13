package by.matskevich.calendaroffactory.mappingHoliday;

import android.content.res.Resources;
import android.util.SparseArray;
import android.view.Menu;
import android.view.SubMenu;
import android.widget.PopupMenu;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.TypeShift;
import by.matskevich.calendaroffactory.util.Utils;

class GeneratingMenuHelper {

    private final Resources resources;

    GeneratingMenuHelper(Resources resources) {

        this.resources = resources;
    }

    void generatingMenu(PopupMenu popupMenu, SparseArray<CharShift> charShiftMap) {

        int id = 10;
        Menu menu = popupMenu.getMenu();
        for (TypeShift typeShift : TypeShift.values()) {
            SubMenu subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, typeShift.ordinal() + 1,
                    Utils.getNameOfTypeShift(typeShift, resources));
            for (CharShift charShift : typeShift.charShift.getEnumConstants()) {
                subMenu.add(Menu.NONE, id, charShift.getOrder() + 1, charShift.getNameChar());
                charShiftMap.put(id, charShift);
                id = id + 5;
            }
        }

    }
}
