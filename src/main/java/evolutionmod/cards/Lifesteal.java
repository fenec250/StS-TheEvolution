package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class Lifesteal
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Lifesteal";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SuccubusAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 9;
    private static final int UPGRADE_DAMAGE_AMT = 3;

    public Lifesteal() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean inForm = isPlayerInThisForm(SuccubusGene.ID);
        addToBot(new LifestealAction(
                p, m, inForm, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        formEffect(SuccubusGene.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Lifesteal();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(SuccubusGene.ID)) {
            this.glowColor = SuccubusGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
    public static class LifestealAction extends AbstractGameAction {

        private DamageInfo info;
        private boolean healSelf;

        public LifestealAction(AbstractCreature source, AbstractCreature target, boolean healSelf, DamageInfo info, AttackEffect effect) {
            this.source = source;
            this.target = target;
            this.info = info;
            this.setValues(target, info);
            this.actionType = ActionType.DAMAGE;
            this.attackEffect = effect;
            this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
            this.healSelf = healSelf;
        }

        public void update() {
            this.tickDuration();
            if (this.isDone) {
                this.target.damage(this.info);
                if (this.target.lastDamageTaken > 0) {
                    if (healSelf) {
                        AbstractDungeon.actionManager.addToTop(new HealAction(this.source, this.source, this.target.lastDamageTaken));
                    } else {
                        AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.source, this.target.lastDamageTaken));
                    }
//				if (!target.isDying) {
//					AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.target, this.source, this.target.lastDamageTaken));
//				}
                }
            }
        }
    }
}
