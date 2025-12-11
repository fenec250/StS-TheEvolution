package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;

public class NightMare3
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:NightMare";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Nightmare.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 9;
    private static final int UPGRADE_DAMAGE_AMT = 3;

    public NightMare3() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        int targetIndex = monsters.indexOf(m);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.multiDamage[targetIndex], this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new ShadowGene2().getChannelAction());
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (int i = 0; i < monsters.size(); ++i) {
                    AbstractMonster monster = monsters.get(i);
                    if (monster.hasPower(WeakPower.POWER_ID)) {
                        addToTop(new DamageAction(
                                monster, new DamageInfo(p, multiDamage[i], damageTypeForTurn),
                                AttackEffect.FIRE));
                    }
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new NightMare3();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

//    @Override
//    public void triggerOnGlowCheck() {
//        if (isPlayerInThisForm(LizardGene.ID)) {
//            this.getGlowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//        } else {
//            this.getGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//        }
//    }
}