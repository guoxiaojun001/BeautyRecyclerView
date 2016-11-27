package cn.lemon.recyclerview.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by HP on 2016/11/26.
 */

public class QQZoomView  extends ListView{
    private ImageView zoomView;
    int heigth;

    public void setZoomView(ImageView zoomView) {
        this.zoomView = zoomView;
    }

    public QQZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        heigth = 150;//设置默认高度
    }


    /**
     * listview焦点改变时--获取iamgeview高度的初始值，该值不能在构造方法中获取
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasWindowFocus){
            this.heigth=zoomView.getHeight();
        }
    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        /**
         * 下拉过度是负数
         * 上拉过度是正的
         */
        if(Math.abs(deltaY)<200){//偏移量太大 就不拉伸了  控制imageview高度的增加
            if(deltaY < 0){
                //下拉过度 不断去改变图的宽高
                zoomView.getLayoutParams().height = zoomView.getHeight() - deltaY;
                //重新布局  比invalidate 效果好
                zoomView.requestLayout();
            }else {
                //上拉过度是正的
                if(zoomView.getHeight() > heigth){
                    zoomView.getLayoutParams().height = zoomView.getHeight() - deltaY;
                    zoomView.requestLayout();

                }
            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    /**
     * 只有在正常滚动的情况触发
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //解决网上推动  没有改变图片大小
        View header = (View) zoomView.getParent();
        //父容器距离到顶部的距离  往上面推的距离
        int deltaY = header.getTop();
        if(deltaY < 0 || zoomView.getHeight() > heigth){
            zoomView.getLayoutParams().height = zoomView.getHeight() + deltaY;
            header.layout(header.getLeft(),0,header.getRight(), header.getHeight());
            //重新布局
            zoomView.requestLayout();
        }
        super.onScrollChanged(l, t, oldl, oldt);

    }

    //回弹
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction()==MotionEvent.ACTION_CANCEL){
            //回弹动画
            ResetAnim resetAnim =    new ResetAnim(zoomView,heigth);
            resetAnim.setDuration(200);

            zoomView.startAnimation(resetAnim);
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 插件化框架：APKPlugin  免费，专门的公司维护      还不错
     */

    public class ResetAnim extends Animation{
        private ImageView header_iv;
        private int currentHeight;
        private int targetHeight;
        private int poorHeight;

        public ResetAnim(ImageView iv,int targetHeight){

            this.header_iv=iv;
            this.targetHeight=targetHeight;
            this.currentHeight=iv.getHeight();
            this.poorHeight=this.currentHeight-this.targetHeight;

        }

        /**
         * 动画执行期间执行该方法，不断执行
         * interpolatedTime：当前时间与duration的时间比（时间执行百分比）
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            //控件的高 = 现在图片的高度差*interpolatedTime
            super.applyTransformation(interpolatedTime, t);
            this.header_iv.getLayoutParams().height=(int)(currentHeight-poorHeight*interpolatedTime);
            this.header_iv.requestLayout();
        }
    }
}
