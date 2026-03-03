/* ============================================================
   CHARTS — charts.js
   Lightweight canvas-based bar & donut charts.
   No external libraries — pure Canvas 2D API.
   ============================================================ */

(function () {
    'use strict';

    /* ---------- Helpers ---------- */
    function getAccentColor() {
        return getComputedStyle(document.documentElement).getPropertyValue('--accent').trim() || '#00d4ff';
    }

    function getTextColor() {
        return getComputedStyle(document.documentElement).getPropertyValue('--text-secondary').trim() || '#94a3b8';
    }

    function getMutedColor() {
        return getComputedStyle(document.documentElement).getPropertyValue('--text-muted').trim() || '#64748b';
    }

    function getBgColor() {
        return getComputedStyle(document.documentElement).getPropertyValue('--bg-secondary').trim() || '#111827';
    }

    /* ---------- Bar Chart ---------- */
    /**
     * Draw a bar chart on a canvas element.
     * @param {string} canvasId - ID of the canvas element.
     * @param {Object} config   - { labels: string[], data: number[], title?: string }
     */
    function drawBarChart(canvasId, config) {
        var canvas = document.getElementById(canvasId);
        if (!canvas) return;

        var ctx = canvas.getContext('2d');
        var dpr = window.devicePixelRatio || 1;

        // Size to container
        var rect = canvas.parentElement.getBoundingClientRect();
        canvas.width = rect.width * dpr;
        canvas.height = 250 * dpr;
        canvas.style.width = rect.width + 'px';
        canvas.style.height = '250px';
        ctx.scale(dpr, dpr);

        var w = rect.width;
        var h = 250;
        var padding = { top: 30, right: 20, bottom: 40, left: 50 };
        var chartW = w - padding.left - padding.right;
        var chartH = h - padding.top - padding.bottom;
        var labels = config.labels || [];
        var data = config.data || [];
        var maxVal = Math.max.apply(null, data) * 1.15;

        // Clear
        ctx.clearRect(0, 0, w, h);

        // Grid lines
        ctx.strokeStyle = getMutedColor() + '33';
        ctx.lineWidth = 1;
        for (var i = 0; i <= 4; i++) {
            var y = padding.top + chartH - (chartH / 4) * i;
            ctx.beginPath();
            ctx.moveTo(padding.left, y);
            ctx.lineTo(w - padding.right, y);
            ctx.stroke();

            // Y-axis labels
            ctx.fillStyle = getMutedColor();
            ctx.font = '11px Inter, sans-serif';
            ctx.textAlign = 'right';
            ctx.fillText(Math.round((maxVal / 4) * i), padding.left - 8, y + 4);
        }

        // Bars
        var barCount = data.length;
        var gap = 12;
        var barW = (chartW - gap * (barCount + 1)) / barCount;

        var accent = getAccentColor();

        data.forEach(function (val, idx) {
            var barH = (val / maxVal) * chartH;
            var x = padding.left + gap + (barW + gap) * idx;
            var y = padding.top + chartH - barH;

            // Bar gradient
            var grad = ctx.createLinearGradient(x, y, x, padding.top + chartH);
            grad.addColorStop(0, accent);
            grad.addColorStop(1, accent + '44');
            ctx.fillStyle = grad;

            // Rounded top rect
            var r = Math.min(4, barW / 2);
            ctx.beginPath();
            ctx.moveTo(x + r, y);
            ctx.lineTo(x + barW - r, y);
            ctx.quadraticCurveTo(x + barW, y, x + barW, y + r);
            ctx.lineTo(x + barW, padding.top + chartH);
            ctx.lineTo(x, padding.top + chartH);
            ctx.lineTo(x, y + r);
            ctx.quadraticCurveTo(x, y, x + r, y);
            ctx.fill();

            // X-axis label
            ctx.fillStyle = getTextColor();
            ctx.font = '11px Inter, sans-serif';
            ctx.textAlign = 'center';
            ctx.fillText(labels[idx] || '', x + barW / 2, h - padding.bottom + 18);
        });

        // Title
        if (config.title) {
            ctx.fillStyle = getTextColor();
            ctx.font = 'bold 13px Inter, sans-serif';
            ctx.textAlign = 'left';
            ctx.fillText(config.title, padding.left, 18);
        }
    }

    /* ---------- Donut Chart ---------- */
    /**
     * Draw a donut chart on a canvas element.
     * @param {string} canvasId - ID of the canvas element.
     * @param {Object} config   - { labels: string[], data: number[], colors?: string[], centerText?: string }
     */
    function drawDonutChart(canvasId, config) {
        var canvas = document.getElementById(canvasId);
        if (!canvas) return;

        var ctx = canvas.getContext('2d');
        var dpr = window.devicePixelRatio || 1;

        var size = Math.min(canvas.parentElement.getBoundingClientRect().width, 250);
        canvas.width = size * dpr;
        canvas.height = size * dpr;
        canvas.style.width = size + 'px';
        canvas.style.height = size + 'px';
        ctx.scale(dpr, dpr);

        var cx = size / 2;
        var cy = size / 2;
        var outerR = (size / 2) - 10;
        var innerR = outerR * 0.62;

        var data = config.data || [];
        var labels = config.labels || [];
        var total = data.reduce(function (s, v) { return s + v; }, 0);
        var defaultColors = ['#00d4ff', '#6366f1', '#22c55e', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'];
        var colors = config.colors || defaultColors;

        ctx.clearRect(0, 0, size, size);

        var startAngle = -Math.PI / 2;

        data.forEach(function (val, idx) {
            var slice = (val / total) * Math.PI * 2;
            ctx.beginPath();
            ctx.arc(cx, cy, outerR, startAngle, startAngle + slice);
            ctx.arc(cx, cy, innerR, startAngle + slice, startAngle, true);
            ctx.closePath();
            ctx.fillStyle = colors[idx % colors.length];
            ctx.fill();

            // Label on arc
            if (slice > 0.3) {
                var midAngle = startAngle + slice / 2;
                var labelR = (outerR + innerR) / 2;
                var lx = cx + Math.cos(midAngle) * labelR;
                var ly = cy + Math.sin(midAngle) * labelR;
                ctx.fillStyle = '#fff';
                ctx.font = 'bold 11px Inter, sans-serif';
                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                ctx.fillText(Math.round((val / total) * 100) + '%', lx, ly);
            }

            startAngle += slice;
        });

        // Center text
        var ct = config.centerText || total.toString();
        ctx.fillStyle = getTextColor();
        ctx.font = 'bold 22px Inter, sans-serif';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(ct, cx, cy - 6);

        ctx.fillStyle = getMutedColor();
        ctx.font = '11px Inter, sans-serif';
        ctx.fillText('Total', cx, cy + 14);

        // Legend below canvas
        var legend = canvas.parentElement.querySelector('.chart-legend');
        if (legend) {
            legend.innerHTML = '';
            labels.forEach(function (label, idx) {
                var item = document.createElement('span');
                item.style.cssText = 'display:inline-flex;align-items:center;gap:6px;font-size:12px;color:' + getTextColor();
                item.innerHTML = '<span style="width:10px;height:10px;border-radius:50%;background:' + colors[idx % colors.length] + ';display:inline-block;"></span>' + label;
                legend.appendChild(item);
            });
        }
    }

    /* ---------- Expose globally ---------- */
    window.Charts = {
        drawBarChart: drawBarChart,
        drawDonutChart: drawDonutChart
    };

    /* Redraw on theme change */
    var observer = new MutationObserver(function () {
        document.querySelectorAll('[data-chart]').forEach(function (canvas) {
            var event = new CustomEvent('redraw');
            canvas.dispatchEvent(event);
        });
    });
    observer.observe(document.documentElement, { attributes: true, attributeFilter: ['data-theme'] });
})();
