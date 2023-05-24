//package com.mygdx.zombieland.effects;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.mygdx.zombieland.World;
//import com.mygdx.zombieland.location.Location;
//import com.mygdx.zombieland.location.Vector2D;
//
//import java.util.List;
//
//public class FollowIndicator {
//
//        private final Location location;
//        private final long created;
//        private final String text;
//        private final Color color;
//        private boolean isVisible;
//
//        public FollowIndicator(Location location, String text, Color color) {
//            this.location = location;
//
//            this.created = System.currentTimeMillis();
//            this.text = text;
//
//            this.color = color;
//        }
//
//        public Location getLocation() {
//            return location;
//        }
//
//        public long getDuration() {
//            return duration;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public long getCreated() {
//            return created;
//        }
//
//        public float getSpeed() {
//            return speed;
//        }
//
//        public Color getColor() {
//            return color;
//        }
//
//    @Override
//    public void create() {
//
//    }
//
//    @Override
//    public void render() {
//        for (TextIndicator.TextItem textItem : items) {
//            if (System.currentTimeMillis() < (textItem.created + textItem.duration)) {
//                if (textItem.fraction < 1) {
//                    textItem.fraction += Gdx.graphics.getDeltaTime() * textItem.speed;
//                    textItem.location.x += (textItem.destination.x - textItem.location.x) * textItem.fraction;
//                    textItem.location.y += (textItem.destination.y - textItem.location.y) * textItem.fraction;
//                }
//                this.world.getFont().setColor((textItem.color == null)
//                        ? new Color(1, .1f,0,.6f)
//                        : textItem.color);
//
//                this.world.getFont().draw(this.world.getBatch(),
//                        textItem.text,
//                        textItem.location.x,
//                        textItem.location.y
//                );
//            } else {
//                items.remove(textItem);
//            }
//        }
//    }
//
//    @Override
//    public void dispose() {
//        this.world.getFont().dispose();
//    }
//}
