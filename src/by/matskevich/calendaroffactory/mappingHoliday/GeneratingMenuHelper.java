package by.matskevich.calendaroffactory.mappingHoliday;

import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.TypeShift;

public class GeneratingMenuHelper {

    public static void generatingMenu(final PopupMenu popupMenu, final View.OnClickListener listener) {

        Menu menu = popupMenu.getMenu();
        for (TypeShift typeShift : TypeShift.values()) {
            SubMenu subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, typeShift.ordinal() + 1, typeShift.numberOfShiftStr);
            for (CharShift charShift : typeShift.charShift.getEnumConstants()) {
                subMenu.add(Menu.NONE, Menu.NONE, charShift.getOrder() + 1, charShift.getNameChar());
            }
        }

    }
}
