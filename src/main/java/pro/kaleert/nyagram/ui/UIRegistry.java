package pro.kaleert.nyagram.ui;

import pro.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
import pro.kaleert.nyagram.util.keyboard.InlineKeyboardBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Реестр и генератор декларативных меню.
 *
 * @since 1.2.0
 */
@Component
@RequiredArgsConstructor
public class UIRegistry implements BeanPostProcessor {

    private final Map<String, Object> menus = new ConcurrentHashMap<>();
    private final Map<String, InlineKeyboardMarkup> prebuiltKeyboards = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Menu menuAnn = AnnotatedElementUtils.findMergedAnnotation(bean.getClass(), Menu.class);
        if (menuAnn != null) {
            menus.put(menuAnn.id(), bean);
            buildKeyboard(menuAnn.id(), bean);
        }
        return bean;
    }

    private void buildKeyboard(String menuId, Object bean) {
        InlineKeyboardBuilder builder = InlineKeyboardBuilder.create();
        
        List<Method> buttons = Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(MenuButton.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(MenuButton.class).row()))
                .collect(Collectors.toList());

        int currentRow = -1;
        for (Method method : buttons) {
            MenuButton btn = method.getAnnotation(MenuButton.class);
            if (currentRow != -1 && btn.row() != currentRow) {
                builder.row();
            }
            builder.button(btn.text(), "ui:" + menuId + ":" + method.getName(), btn.style());
            currentRow = btn.row();
        }

        prebuiltKeyboards.put(menuId, builder.build());
    }

    /**
     * Возвращает готовую клавиатуру для указанного меню.
     *
     * @param menuId Идентификатор меню.
     * @return InlineKeyboardMarkup или null.
     */
    public InlineKeyboardMarkup getKeyboard(String menuId) {
        return prebuiltKeyboards.get(menuId);
    }

    /**
     * Возвращает экземпляр бина (контроллера) меню по его ID.
     *
     * @param menuId Идентификатор меню.
     * @return Бин или null.
     */
    public Object getMenuBean(String menuId) {
        return menus.get(menuId);
    }
}