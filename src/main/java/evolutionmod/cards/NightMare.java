package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.CentaurGene2;
import evolutionmod.patches.EvolutionEnum;

import java.util.Arrays;

public class NightMare
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:NightMare";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Nightmare.png";
    private static final int COST = 2;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 2;

    public NightMare() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new NightMareAction(
                p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        formEffect(CentaurGene2.ID);
    }

    @Override
    public void applyPowers() {
        this.baseDamage = DAMAGE + (upgraded ? UPGRADE_DAMAGE : 0) + (isPlayerInThisForm(CentaurGene2.ID) ? GameActionManager.turn : 0);
        super.applyPowers();
        this.baseDamage = DAMAGE + (upgraded ? UPGRADE_DAMAGE : 0);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new NightMare();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(CentaurGene2.ID)) {
            this.glowColor = CentaurGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }


    public static class NightMareAction extends AbstractGameAction {

        private DamageInfo info;

        public NightMareAction(AbstractCreature source, AbstractCreature target, DamageInfo info, AttackEffect effect) {
            this.source = source;
            this.target = target;
            this.info = info;
            this.setValues(target, info);
            this.actionType = ActionType.DAMAGE;
            this.attackEffect = effect;
            this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        }

        public void update() {
            this.tickDuration();
            if (this.isDone) {
                this.target.damage(this.info);
                if (this.target.lastDamageTaken/2 > 0) {
                    int[] multiDamage = new int[AbstractDungeon.getMonsters().monsters.size()];
                    Arrays.fill(multiDamage, this.target.lastDamageTaken);
                    addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage,
                            DamageInfo.DamageType.THORNS,
                            AbstractGameAction.AttackEffect.FIRE));
                }
            }
        }
    }
}